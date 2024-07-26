package com.tomgrx.shortlink.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomgrx.shortlink.core.common.convention.exception.ClientException;
import com.tomgrx.shortlink.core.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.core.common.enums.ValidDateTypeEnum;
import com.tomgrx.shortlink.core.config.WhiteListConfiguration;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.core.dao.entity.GotoDO;
import com.tomgrx.shortlink.core.dao.mapper.GotoMapper;
import com.tomgrx.shortlink.core.dao.mapper.ShortlinkMapper;
import com.tomgrx.shortlink.core.dto.biz.StatsRecordDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkUpdateReqDTO;
import com.tomgrx.shortlink.core.dto.resp.*;
import com.tomgrx.shortlink.core.mq.producer.RedisStreamStatsProducer;
import com.tomgrx.shortlink.core.service.ShortlinkService;
import com.tomgrx.shortlink.core.toolkit.HashUtil;
import com.tomgrx.shortlink.core.toolkit.LinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.*;
import static com.tomgrx.shortlink.constant.ShortlinkConstant.DEFAULT_MAX_RETRY;
import static com.tomgrx.shortlink.constant.ShortlinkConstant.NULL_GOTO_CACHE_VALID_DURATION;
import static com.tomgrx.shortlink.constant.UserConstant.COOKIE_VALID_DURATION;

/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortlinkServiceImpl extends ServiceImpl<ShortlinkMapper, ShortlinkDO> implements ShortlinkService {

    private final RBloomFilter<String> lidBloomFilter;
    private final GotoMapper gotoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final RedisStreamStatsProducer redisStreamStatsProducer;
    private final WhiteListConfiguration whiteListConfiguration;

    @Value("${shortlink.max-retry}")
    private Integer maxRetry = DEFAULT_MAX_RETRY;

    /**
     * 创建短链接（使用布隆过滤器）
     * <p>
     * 常见问题：
     * 1. 短链接接口的并发量有多少？如何测试？
     * 2. 短链接项目有多少数据？如何解决海量数据存储？
     * 3. 短链接数据库分片键是如何考虑的？
     * 4. 项目中短链接缓存预热是怎么做的？
     * 5. 删除短链接后，布隆过滤器如何删除？
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShortlinkCreateRespDTO createShortlink(ShortlinkCreateReqDTO requestParam) {
        // 如果白名单功能开启，拒绝域名不在白名单的请求
        checkWhiteList(requestParam.getOriginUrl());

        // 创建短链接标识符
        String lid = createLid(requestParam);

        // 在数据库中创建短链接
        ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createType(requestParam.getCreateType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .lid(lid)
                .enableFlag(0)
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .deleteTime(0L)
                .favicon(getFavicon(requestParam.getOriginUrl()))
                .build();
        GotoDO linkGotoDO = GotoDO.builder()
                .lid(lid)
                .gid(requestParam.getGid())
                .build();
        try {
            baseMapper.insert(shortlinkDO);
            gotoMapper.insert(linkGotoDO);
        } catch (DuplicateKeyException ex) {
            // 首先判断是否存在布隆过滤器，如果不存在直接新增
            if (!lidBloomFilter.contains(lid)) {
                lidBloomFilter.add(lid);
            }
            throw new ServiceException(String.format("短链接 %s 生成重复", lid));
        }

        // 在缓存中创建短链接
        stringRedisTemplate.opsForValue().set(
                GOTO_KEY_PREFIX + lid,
                requestParam.getOriginUrl(),
                LinkUtil.getLinkCacheValidTime(requestParam.getValidDate()),
                TimeUnit.MILLISECONDS
        );
        lidBloomFilter.add(lid);

        return ShortlinkCreateRespDTO.builder()
                .lid(lid)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

    /**
     * 创建短链接（使用分布式锁）
     * <p>
     * 常见问题：
     * 1. 为什么说布隆过滤器性能远胜于分布式锁？
     */
    @Override
    public ShortlinkCreateRespDTO createShortlinkByLock(ShortlinkCreateReqDTO requestParam) {
        // 如果白名单功能开启，拒绝域名不在白名单的请求
        checkWhiteList(requestParam.getOriginUrl());

        // 创建短链接标识符
        String lid = createLid(requestParam);

        ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createType(requestParam.getCreateType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .lid(lid)
                .enableFlag(0)
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .deleteTime(0L)
                .favicon(getFavicon(requestParam.getOriginUrl()))
                .build();
        GotoDO linkGotoDO = GotoDO.builder()
                .lid(lid)
                .gid(requestParam.getGid())
                .build();

        // 使用分布式锁保证创建短链接不会遇到并发冲突
        RLock lock = redissonClient.getLock(LOCK_CREATE_SHORTLINK_KEY);
        lock.lock();
        try {
            // 在数据库中创建短链接
            try {
                baseMapper.insert(shortlinkDO);
                gotoMapper.insert(linkGotoDO);
            } catch (DuplicateKeyException ex) {
                throw new ServiceException(String.format("短链接：%s 生成重复", lid));
            }

            // 在缓存中创建短链接
            stringRedisTemplate.opsForValue().set(
                    GOTO_KEY_PREFIX + lid,
                    requestParam.getOriginUrl(),
                    LinkUtil.getLinkCacheValidTime(requestParam.getValidDate()), TimeUnit.MILLISECONDS
            );
        } finally {
            lock.unlock();
        }

        return ShortlinkCreateRespDTO.builder()
                .lid(lid)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

    /**
     * 批量创建短链接
     */
    @Override
    public ShortlinkBatchCreateRespDTO batchCreateShortlink(ShortlinkBatchCreateReqDTO requestParam) {
        List<String> originUrls = requestParam.getOriginUrls();
        List<String> describes = requestParam.getDescribes();
        List<ShortlinkBaseInfoRespDTO> result = new ArrayList<>();
        for (int i = 0; i < originUrls.size(); i++) {
            ShortlinkCreateReqDTO shortlinkCreateReqDTO = BeanUtil.toBean(requestParam, ShortlinkCreateReqDTO.class);
            shortlinkCreateReqDTO.setOriginUrl(originUrls.get(i));
            shortlinkCreateReqDTO.setDescribe(describes.get(i));
            try {
                ShortlinkCreateRespDTO shortlink = createShortlink(shortlinkCreateReqDTO);
                ShortlinkBaseInfoRespDTO linkBaseInfoRespDTO = ShortlinkBaseInfoRespDTO.builder()
                        .lid(shortlink.getLid())
                        .originUrl(shortlink.getOriginUrl())
                        .describe(describes.get(i))
                        .build();
                result.add(linkBaseInfoRespDTO);
            } catch (Throwable ex) {
                log.error("批量创建短链接失败，原始链接：{}", originUrls.get(i));
            }
        }
        return ShortlinkBatchCreateRespDTO.builder()
                .total(result.size())
                .baseLinkInfos(result)
                .build();
    }

    /**
     * 修改短链接
     * <p>
     * 常见问题：
     * 1. 为什么监控表要加上 gid？不加的话是否就不存在读写锁？
     * 2. 短链接如何保障缓存和数据库一致性？
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortlink(ShortlinkUpdateReqDTO requestParam) {
        // 如果白名单功能开启，拒绝域名不在白名单的请求
        checkWhiteList(requestParam.getOriginUrl());

        // 拒绝修改不存在的短链接
        LambdaQueryWrapper<ShortlinkDO> queryWrapper = Wrappers.lambdaQuery(ShortlinkDO.class)
                .eq(ShortlinkDO::getGid, requestParam.getOriginGid())
                .eq(ShortlinkDO::getLid, requestParam.getLid())
                .eq(ShortlinkDO::getDeleteFlag, 0)
                .eq(ShortlinkDO::getEnableFlag, 0);
        ShortlinkDO hasShortlinkDO = baseMapper.selectOne(queryWrapper);
        if (hasShortlinkDO == null) {
            throw new ClientException("短链接记录不存在");
        }

        // 修改数据库中的短链接信息
        if (Objects.equals(hasShortlinkDO.getGid(), requestParam.getGid())) {
            // 如果无需修改 gid
            LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                    .eq(ShortlinkDO::getLid, requestParam.getLid())
                    .eq(ShortlinkDO::getGid, requestParam.getGid())
                    .eq(ShortlinkDO::getDeleteFlag, 0)
                    .eq(ShortlinkDO::getEnableFlag, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()), ShortlinkDO::getValidDate, null);
            ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                    .favicon(hasShortlinkDO.getFavicon())
                    .createType(hasShortlinkDO.getCreateType())
                    .originUrl(Optional.ofNullable(requestParam.getOriginUrl()).orElse(hasShortlinkDO.getOriginUrl()))
                    .describe(Optional.ofNullable(requestParam.getDescribe()).orElse(hasShortlinkDO.getDescribe()))
                    .validDateType(Optional.ofNullable(requestParam.getValidDateType()).orElse(hasShortlinkDO.getValidDateType()))
                    .validDate(Optional.ofNullable(requestParam.getValidDate()).orElse(hasShortlinkDO.getValidDate()))
                    .build();
            baseMapper.update(shortlinkDO, updateWrapper);
        } else {
            // 如果需要修改 gid
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(LOCK_GID_UPDATE_KEY_PREFIX + requestParam.getLid());
            RLock rLock = readWriteLock.writeLock();
            rLock.lock();
            try {
                // 删除原有的 t_link 记录
                LambdaUpdateWrapper<ShortlinkDO> linkUpdateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                        .eq(ShortlinkDO::getLid, requestParam.getLid())
                        .eq(ShortlinkDO::getGid, hasShortlinkDO.getGid())
                        .eq(ShortlinkDO::getDeleteFlag, 0)
                        .eq(ShortlinkDO::getDeleteTime, 0L)
                        .eq(ShortlinkDO::getEnableFlag, 0);
                ShortlinkDO delShortlinkDO = ShortlinkDO.builder()
                        .deleteTime(System.currentTimeMillis())
                        .build();
                delShortlinkDO.setDeleteFlag(1);
                baseMapper.update(delShortlinkDO, linkUpdateWrapper);

                // 增加新的 t_link 记录
                ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                        .originUrl(Optional.ofNullable(requestParam.getOriginUrl()).orElse(hasShortlinkDO.getOriginUrl()))
                        .gid(requestParam.getGid())
                        .lid(hasShortlinkDO.getLid())
                        .createType(hasShortlinkDO.getCreateType())
                        .validDateType(Optional.ofNullable(requestParam.getValidDateType()).orElse(hasShortlinkDO.getValidDateType()))
                        .validDate(Optional.ofNullable(requestParam.getValidDate()).orElse(hasShortlinkDO.getValidDate()))
                        .describe(Optional.ofNullable(requestParam.getDescribe()).orElse(hasShortlinkDO.getDescribe()))
                        .lid(hasShortlinkDO.getLid())
                        .enableFlag(hasShortlinkDO.getEnableFlag())
                        .totalPv(hasShortlinkDO.getTotalPv())
                        .totalUv(hasShortlinkDO.getTotalUv())
                        .totalUip(hasShortlinkDO.getTotalUip())
                        .favicon(getFavicon(requestParam.getOriginUrl()))
                        .deleteTime(0L)
                        .build();
                baseMapper.insert(shortlinkDO);

                // 删除原有的 t_goto 记录
                LambdaQueryWrapper<GotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(GotoDO.class)
                        .eq(GotoDO::getLid, requestParam.getLid())
                        .eq(GotoDO::getGid, hasShortlinkDO.getGid());
                GotoDO gotoDO = gotoMapper.selectOne(linkGotoQueryWrapper);
                gotoMapper.delete(linkGotoQueryWrapper);

                // 增加新的 t_goto 记录
                gotoDO.setGid(requestParam.getGid());
                gotoMapper.insert(gotoDO);
            } finally {
                rLock.unlock();
            }
        }

        // 删除过时缓存
        // TODO: 需要重构逻辑
        if (Objects.equals(hasShortlinkDO.getValidDateType(), requestParam.getValidDateType())
                && Objects.equals(hasShortlinkDO.getValidDate(), requestParam.getValidDate())
                && Objects.equals(hasShortlinkDO.getOriginUrl(), requestParam.getOriginUrl())) {
            return;
        }
        stringRedisTemplate.delete(GOTO_KEY_PREFIX + requestParam.getLid());
        Date currentDate = new Date();
        if (hasShortlinkDO.getValidDate() == null || currentDate.before(hasShortlinkDO.getValidDate())) {
            return;
        }
        if (Objects.equals(requestParam.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()) || requestParam.getValidDate().after(currentDate)) {
            stringRedisTemplate.delete(NULL_GOTO_KEY_PREFIX + requestParam.getLid());
        }
    }

    /**
     * 分页查询短链接
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IPage<ShortlinkPageRespDTO> pageShortlink(ShortlinkPageReqDTO requestParam) {
        return baseMapper.pageLink(requestParam);
    }

    /**
     * 查询分组的短链接数量
     */
    @Override
    public List<GroupCountQueryRespDTO> listGroupShortlinkCount(List<String> gidList) {
        QueryWrapper<ShortlinkDO> queryWrapper = Wrappers.query(new ShortlinkDO())
                .select("gid as gid, count(*) as count")
                .in("gid", gidList)
                .eq("enable_flag", 0)
                .eq("delete_flag", 0)
                .eq("delete_time", 0L)
                .groupBy("gid");
        List<Map<String, Object>> shortlinkDOList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(shortlinkDOList, GroupCountQueryRespDTO.class);
    }

    /**
     * 短链接跳转原始链接
     * <p>
     * 常见问题：
     * 1. 短链接接口的并发量有多少？如何测试？
     * 2. 短链接是如何跳转长链接？
     */
    @SneakyThrows
    @Override
    public void gotoUrl(String lid, HttpServletRequest request, HttpServletResponse response) {
        // 如果缓存有原始链接，直接跳转
        String originLink = stringRedisTemplate.opsForValue().get(GOTO_KEY_PREFIX + lid);
        if (StrUtil.isNotBlank(originLink)) {
            createShortlinkStatsRecord(lid, request, response);
            response.sendRedirect(originLink);
            return;
        }

        // 拒绝不存在的短链接
        if (!lidBloomFilter.contains(lid)) {
            response.sendRedirect("/page/notfound");
            return;
        }

        // 拒绝空值短链接
        String gotoIsNullShortlink = stringRedisTemplate.opsForValue().get(NULL_GOTO_KEY_PREFIX + lid);
        if (StrUtil.isNotBlank(gotoIsNullShortlink)) {
            response.sendRedirect("/page/notfound");
            return;
        }

        // 加锁保证同一时刻对于同一个短链接标识符只有一个请求能访问数据库
        RLock lock = redissonClient.getLock(LOCK_GOTO_KEY_PREFIX + lid);
        lock.lock();
        try {
            // 再次检查缓存，如果有原始链接，直接跳转
            originLink = stringRedisTemplate.opsForValue().get(GOTO_KEY_PREFIX + lid);
            if (StrUtil.isNotBlank(originLink)) {
                createShortlinkStatsRecord(lid, request, response);
                response.sendRedirect(originLink);
                return;
            }

            // 再次检查缓存，拒绝不存在的短链接
            gotoIsNullShortlink = stringRedisTemplate.opsForValue().get(NULL_GOTO_KEY_PREFIX + lid);
            if (StrUtil.isNotBlank(gotoIsNullShortlink)) {
                response.sendRedirect("/page/notfound");
                return;
            }

            // 拒绝不存在或过期的短链接，并且在缓存中设置该短链接为空值跳转
            LambdaQueryWrapper<GotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(GotoDO.class)
                    .eq(GotoDO::getLid, lid);
            GotoDO gotoDO = gotoMapper.selectOne(linkGotoQueryWrapper);
            if (gotoDO == null) {
                stringRedisTemplate.opsForValue().set(NULL_GOTO_KEY_PREFIX + lid, "-", NULL_GOTO_CACHE_VALID_DURATION, TimeUnit.MINUTES);
                response.sendRedirect("/page/notfound");
                return;
            }
            LambdaQueryWrapper<ShortlinkDO> queryWrapper = Wrappers.lambdaQuery(ShortlinkDO.class)
                    .eq(ShortlinkDO::getGid, gotoDO.getGid())
                    .eq(ShortlinkDO::getLid, lid)
                    .eq(ShortlinkDO::getDeleteFlag, 0)
                    .eq(ShortlinkDO::getEnableFlag, 0);
            ShortlinkDO shortlinkDO = baseMapper.selectOne(queryWrapper);
            if (shortlinkDO == null || (shortlinkDO.getValidDate() != null && shortlinkDO.getValidDate().before(new Date()))) {
                stringRedisTemplate.opsForValue().set(NULL_GOTO_KEY_PREFIX + lid, "-", NULL_GOTO_CACHE_VALID_DURATION, TimeUnit.MINUTES);
                response.sendRedirect("/page/notfound");
                return;
            }

            // 在缓存中设置该短链接和原始链接
            stringRedisTemplate.opsForValue().set(
                    GOTO_KEY_PREFIX + lid,
                    shortlinkDO.getOriginUrl(),
                    LinkUtil.getLinkCacheValidTime(shortlinkDO.getValidDate()),
                    TimeUnit.MILLISECONDS
            );
            createShortlinkStatsRecord(lid, request, response);
            response.sendRedirect(shortlinkDO.getOriginUrl());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 创建短链接监控记录
     * 副作用：如果Cookie中没有访客标识，则创建访客标识并存储于Cookie中
     *
     * @param lid      短链接标识符
     * @param request  HTTP 请求
     * @param response HTTP 响应
     */
    private void createShortlinkStatsRecord(String lid, HttpServletRequest request, HttpServletResponse response) {
        // 获取访客标识
        AtomicReference<String> vid = new AtomicReference<>();
        AtomicBoolean isVisitorFirstVisit = new AtomicBoolean();
        Cookie[] cookies = request.getCookies();
        Runnable addResponseCookieTask = () -> {
            vid.set(UUID.fastUUID().toString());
            Cookie cookie = new Cookie("vid", vid.get());   // 如果访客初次访问，则生成访客标识（vid）并在 cookie 中存储。
            cookie.setMaxAge(COOKIE_VALID_DURATION);
            cookie.setPath(lid);
            response.addCookie(cookie);
            isVisitorFirstVisit.set(Boolean.TRUE);
            stringRedisTemplate.opsForSet().add(UV_STATS_KEY_PREFIX + lid, vid.get());
        };
        Arrays.stream(cookies)
                .filter(each -> Objects.equals(each.getName(), "vid"))  // 如果非初次访问，则可在 cookie 中读取 vid
                .findFirst()
                .map(Cookie::getValue)
                .ifPresentOrElse(each -> {
                    vid.set(each);
                    Long visitorAddNum = stringRedisTemplate.opsForSet().add(UV_STATS_KEY_PREFIX + lid, each);
                    isVisitorFirstVisit.set(visitorAddNum != null && visitorAddNum > 0L);
                }, addResponseCookieTask);

        // 获取访客 IP
        String vip = LinkUtil.getIp(request);
        Long uipAddNum = stringRedisTemplate.opsForSet().add(UIP_STATS_KEY_PREFIX + lid, vip);
        boolean isIpFirstVisit = uipAddNum != null && uipAddNum > 0L;

        // 获取其他统计数据
        String os = LinkUtil.getOs(request);
        String browser = LinkUtil.getBrowser(request);
        String device = LinkUtil.getDevice(request);
        String network = LinkUtil.getNetwork(request);

        // 发送统计数据到消息队列，异步保存监控记录
        StatsRecordDTO statsRecord = StatsRecordDTO.builder()
                .lid(lid)
                .vid(vid.get())
                .vip(vip)
                .isVisitorFirstVisit(isVisitorFirstVisit.get())
                .isIpFirstVisit(isIpFirstVisit)
                .os(os)
                .browser(browser)
                .device(device)
                .network(network)
                .currentDate(new Date())
                .build();
        sendStatsRecord(statsRecord);
    }

    /**
     * 发送统计数据到消息队列
     * 常见问题：
     * 1. 为什么选择 RocketMQ？比较 Redis Stream，RocketMQ 和 Kafka？
     *
     * @param statsRecord 短链接统计数据实体参数
     */
    @Override
    public void sendStatsRecord(StatsRecordDTO statsRecord) {
        Map<String, String> producerMap = new HashMap<>();
        producerMap.put("statsRecord", JSON.toJSONString(statsRecord));
        redisStreamStatsProducer.send(producerMap);
    }

    /**
     * 创建短链接标识符（使用布隆过滤器）
     * 常见问题：
     * 1. 短链接哈希算法生成冲突问题如何解决？
     * 2. 判断短链接是否存在为什么不使用Set结构？
     * 3. 如果布隆过滤器挂了，里边存的数据全丢失了，怎么恢复？
     */
    private String createLid(ShortlinkCreateReqDTO requestParam) {
        for (int retry = 0; retry < maxRetry; retry++) {
            String lid = HashUtil.hashToBase62(requestParam.getOriginUrl() + UUID.randomUUID());
            if (!lidBloomFilter.contains(lid)) {
                return lid;
            }
        }
        throw new ServiceException("短链接标识符生成过于频繁");
    }

    /**
     * 创建短链接标识符（使用数据库）
     */
    private String createLidByDB(ShortlinkCreateReqDTO requestParam) {
        for (int retry = 0; retry < maxRetry; retry++) {
            String lid = HashUtil.hashToBase62(requestParam.getOriginUrl() + UUID.randomUUID());
            LambdaQueryWrapper<ShortlinkDO> queryWrapper = Wrappers.lambdaQuery(ShortlinkDO.class)
                    .eq(ShortlinkDO::getGid, requestParam.getGid())
                    .eq(ShortlinkDO::getLid, lid)
                    .eq(ShortlinkDO::getDeleteFlag, 0);
            ShortlinkDO shortlinkDO = baseMapper.selectOne(queryWrapper);
            if (shortlinkDO == null) {
                return lid;
            }
        }
        throw new ServiceException("短链接标识符生成过于频繁");
    }

    /**
     * 获取网站图标的链接
     */
    @SneakyThrows
    private String getFavicon(String url) {
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseLid = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseLid) {
            Document document = Jsoup.connect(url).get();
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null) {
                return faviconLink.attr("abs:href");
            }
        }
        return null;
    }

    /**
     * 如果开启域名白名单功能，则拒绝域名不在白名单中的原始链接。
     *
     * @param originUrl 原始链接
     */
    private void checkWhiteList(String originUrl) {
        Boolean enable = whiteListConfiguration.getEnable();
        if (enable == null || !enable) {
            return;
        }

        String domain = LinkUtil.extractDomain(originUrl);
        if (StrUtil.isBlank(domain)) {
            throw new ClientException("跳转链接填写错误");
        }

        List<String> details = whiteListConfiguration.getDomains();
        if (!details.contains(domain)) {
            throw new ClientException(String.format("不支持跳转到 %s。请生成以下网站跳转链接：%s", domain, whiteListConfiguration.getName()));
        }
    }
}
