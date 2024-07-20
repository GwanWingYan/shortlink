package com.tomgrx.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomgrx.shortlink.project.common.convention.exception.ClientException;
import com.tomgrx.shortlink.project.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.project.common.enums.ValidDateTypeEnum;
import com.tomgrx.shortlink.project.config.GotoDomainWhiteListConfiguration;
import com.tomgrx.shortlink.project.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.project.dao.entity.ShortlinkGotoDO;
import com.tomgrx.shortlink.project.dao.mapper.ShortlinkGotoMapper;
import com.tomgrx.shortlink.project.dao.mapper.ShortlinkMapper;
import com.tomgrx.shortlink.project.dto.biz.ShortlinkStatsRecordDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkUpdateReqDTO;
import com.tomgrx.shortlink.project.dto.resp.*;
import com.tomgrx.shortlink.project.mq.producer.ShortlinkStatsSaveProducer;
import com.tomgrx.shortlink.project.service.ShortlinkService;
import com.tomgrx.shortlink.project.toolkit.HashUtil;
import com.tomgrx.shortlink.project.toolkit.LinkUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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

/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortlinkServiceImpl extends ServiceImpl<ShortlinkMapper, ShortlinkDO> implements ShortlinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;
    private final ShortlinkGotoMapper shortlinkGotoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final ShortlinkStatsSaveProducer shortlinkStatsSaveProducer;
    private final GotoDomainWhiteListConfiguration gotoDomainWhiteListConfiguration;

    @Value("${shortlink.domain.default}")
    private String createShortlinkDefaultDomain;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShortlinkCreateRespDTO createShortlink(ShortlinkCreateReqDTO requestParam) {
        // 短链接接口的并发量有多少？如何测试？详情查看：https://tomgrx.com/shortlink/question
        verificationWhitelist(requestParam.getOriginUrl());
        String shortlinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(createShortlinkDefaultDomain)
                .append("/")
                .append(shortlinkSuffix)
                .toString();
        ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                .domain(createShortlinkDefaultDomain)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createdType(requestParam.getCreatedType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortlinkSuffix)
                .enableStatus(0)
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .delTime(0L)
                .fullShortUrl(fullShortUrl)
                .favicon(getFavicon(requestParam.getOriginUrl()))
                .build();
        ShortlinkGotoDO linkGotoDO = ShortlinkGotoDO.builder()
                .fullShortUrl(fullShortUrl)
                .gid(requestParam.getGid())
                .build();
        try {
            // 短链接项目有多少数据？如何解决海量数据存储？详情查看：https://tomgrx.com/shortlink/question
            baseMapper.insert(shortlinkDO);
            // 短链接数据库分片键是如何考虑的？详情查看：https://tomgrx.com/shortlink/question
            shortlinkGotoMapper.insert(linkGotoDO);
        } catch (DuplicateKeyException ex) {
            // 首先判断是否存在布隆过滤器，如果不存在直接新增
            if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
                shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
            }
            throw new ServiceException(String.format("短链接：%s 生成重复", fullShortUrl));
        }
        // 项目中短链接缓存预热是怎么做的？详情查看：https://tomgrx.com/shortlink/question
        stringRedisTemplate.opsForValue().set(
                GOTO_KEY + fullShortUrl,
                requestParam.getOriginUrl(),
                LinkUtil.getLinkCacheValidTime(requestParam.getValidDate()), TimeUnit.MILLISECONDS
        );
        // 删除短链接后，布隆过滤器如何删除？详情查看：https://tomgrx.com/shortlink/question
        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortlinkCreateRespDTO.builder()
                .fullShortUrl("http://" + shortlinkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

    @Override
    public ShortlinkCreateRespDTO createShortlinkByLock(ShortlinkCreateReqDTO requestParam) {
        verificationWhitelist(requestParam.getOriginUrl());
        String fullShortUrl;
        // 为什么说布隆过滤器性能远胜于分布式锁？详情查看：https://tomgrx.com/shortlink/question
        RLock lock = redissonClient.getLock(LOCK_CREATE_SHORTLINK_KEY);
        lock.lock();
        try {
            String shortlinkSuffix = generateSuffixByLock(requestParam);
            fullShortUrl = StrBuilder.create(createShortlinkDefaultDomain)
                    .append("/")
                    .append(shortlinkSuffix)
                    .toString();
            ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                    .domain(createShortlinkDefaultDomain)
                    .originUrl(requestParam.getOriginUrl())
                    .gid(requestParam.getGid())
                    .createdType(requestParam.getCreatedType())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate())
                    .describe(requestParam.getDescribe())
                    .shortUri(shortlinkSuffix)
                    .enableStatus(0)
                    .totalPv(0)
                    .totalUv(0)
                    .totalUip(0)
                    .delTime(0L)
                    .fullShortUrl(fullShortUrl)
                    .favicon(getFavicon(requestParam.getOriginUrl()))
                    .build();
            ShortlinkGotoDO linkGotoDO = ShortlinkGotoDO.builder()
                    .fullShortUrl(fullShortUrl)
                    .gid(requestParam.getGid())
                    .build();
            try {
                baseMapper.insert(shortlinkDO);
                shortlinkGotoMapper.insert(linkGotoDO);
            } catch (DuplicateKeyException ex) {
                throw new ServiceException(String.format("短链接：%s 生成重复", fullShortUrl));
            }
            stringRedisTemplate.opsForValue().set(
                    GOTO_KEY + fullShortUrl,
                    requestParam.getOriginUrl(),
                    LinkUtil.getLinkCacheValidTime(requestParam.getValidDate()), TimeUnit.MILLISECONDS
            );
        } finally {
            lock.unlock();
        }
        return ShortlinkCreateRespDTO.builder()
                .fullShortUrl("http://" + fullShortUrl)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

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
                        .fullShortUrl(shortlink.getFullShortUrl())
                        .originUrl(shortlink.getOriginUrl())
                        .describe(describes.get(i))
                        .build();
                result.add(linkBaseInfoRespDTO);
            } catch (Throwable ex) {
                log.error("批量创建短链接失败，原始参数：{}", originUrls.get(i));
            }
        }
        return ShortlinkBatchCreateRespDTO.builder()
                .total(result.size())
                .baseLinkInfos(result)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortlink(ShortlinkUpdateReqDTO requestParam) {
        verificationWhitelist(requestParam.getOriginUrl());
        LambdaQueryWrapper<ShortlinkDO> queryWrapper = Wrappers.lambdaQuery(ShortlinkDO.class)
                .eq(ShortlinkDO::getGid, requestParam.getOriginGid())
                .eq(ShortlinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortlinkDO::getDelFlag, 0)
                .eq(ShortlinkDO::getEnableStatus, 0);
        ShortlinkDO hasShortlinkDO = baseMapper.selectOne(queryWrapper);
        if (hasShortlinkDO == null) {
            throw new ClientException("短链接记录不存在");
        }
        if (Objects.equals(hasShortlinkDO.getGid(), requestParam.getGid())) {
            LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                    .eq(ShortlinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortlinkDO::getGid, requestParam.getGid())
                    .eq(ShortlinkDO::getDelFlag, 0)
                    .eq(ShortlinkDO::getEnableStatus, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()), ShortlinkDO::getValidDate, null);
            ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                    .domain(hasShortlinkDO.getDomain())
                    .shortUri(hasShortlinkDO.getShortUri())
                    .favicon(hasShortlinkDO.getFavicon())
                    .createdType(hasShortlinkDO.getCreatedType())
                    .gid(requestParam.getGid())
                    .originUrl(requestParam.getOriginUrl())
                    .describe(requestParam.getDescribe())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate())
                    .build();
            baseMapper.update(shortlinkDO, updateWrapper);
        } else {
            // 为什么监控表要加上Gid？不加的话是否就不存在读写锁？详情查看：https://nageoffer.com/shortlink/question
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(LOCK_GID_UPDATE_KEY + requestParam.getFullShortUrl());
            RLock rLock = readWriteLock.writeLock();
            rLock.lock();
            try {
                LambdaUpdateWrapper<ShortlinkDO> linkUpdateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                        .eq(ShortlinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortlinkDO::getGid, hasShortlinkDO.getGid())
                        .eq(ShortlinkDO::getDelFlag, 0)
                        .eq(ShortlinkDO::getDelTime, 0L)
                        .eq(ShortlinkDO::getEnableStatus, 0);
                ShortlinkDO delShortlinkDO = ShortlinkDO.builder()
                        .delTime(System.currentTimeMillis())
                        .build();
                delShortlinkDO.setDelFlag(1);
                baseMapper.update(delShortlinkDO, linkUpdateWrapper);
                ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                        .domain(createShortlinkDefaultDomain)
                        .originUrl(requestParam.getOriginUrl())
                        .gid(requestParam.getGid())
                        .createdType(hasShortlinkDO.getCreatedType())
                        .validDateType(requestParam.getValidDateType())
                        .validDate(requestParam.getValidDate())
                        .describe(requestParam.getDescribe())
                        .shortUri(hasShortlinkDO.getShortUri())
                        .enableStatus(hasShortlinkDO.getEnableStatus())
                        .totalPv(hasShortlinkDO.getTotalPv())
                        .totalUv(hasShortlinkDO.getTotalUv())
                        .totalUip(hasShortlinkDO.getTotalUip())
                        .fullShortUrl(hasShortlinkDO.getFullShortUrl())
                        .favicon(getFavicon(requestParam.getOriginUrl()))
                        .delTime(0L)
                        .build();
                baseMapper.insert(shortlinkDO);
                LambdaQueryWrapper<ShortlinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(ShortlinkGotoDO.class)
                        .eq(ShortlinkGotoDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortlinkGotoDO::getGid, hasShortlinkDO.getGid());
                ShortlinkGotoDO shortlinkGotoDO = shortlinkGotoMapper.selectOne(linkGotoQueryWrapper);
                shortlinkGotoMapper.delete(linkGotoQueryWrapper);
                shortlinkGotoDO.setGid(requestParam.getGid());
                shortlinkGotoMapper.insert(shortlinkGotoDO);
            } finally {
                rLock.unlock();
            }
        }
        // 短链接如何保障缓存和数据库一致性？详情查看：https://tomgrx.com/shortlink/question
        if (!Objects.equals(hasShortlinkDO.getValidDateType(), requestParam.getValidDateType())
                || !Objects.equals(hasShortlinkDO.getValidDate(), requestParam.getValidDate())
                || !Objects.equals(hasShortlinkDO.getOriginUrl(), requestParam.getOriginUrl())) {
            stringRedisTemplate.delete(GOTO_KEY + requestParam.getFullShortUrl());
            Date currentDate = new Date();
            if (hasShortlinkDO.getValidDate() != null && hasShortlinkDO.getValidDate().before(currentDate)) {
                if (Objects.equals(requestParam.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()) || requestParam.getValidDate().after(currentDate)) {
                    stringRedisTemplate.delete(GOTO_IS_NULL_KEY + requestParam.getFullShortUrl());
                }
            }
        }
    }

    @Override
    public IPage<ShortlinkPageRespDTO> pageShortlink(ShortlinkPageReqDTO requestParam) {
        IPage<ShortlinkDO> resultPage = baseMapper.pageLink(requestParam);
        return resultPage.convert(each -> {
            ShortlinkPageRespDTO result = BeanUtil.toBean(each, ShortlinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
    }

    @Override
    public List<ShortlinkGroupCountQueryRespDTO> listGroupShortlinkCount(List<String> requestParam) {
        QueryWrapper<ShortlinkDO> queryWrapper = Wrappers.query(new ShortlinkDO())
                .select("gid as gid, count(*) as shortlinkCount")
                .in("gid", requestParam)
                .eq("enable_status", 0)
                .eq("del_flag", 0)
                .eq("del_time", 0L)
                .groupBy("gid");
        List<Map<String, Object>> shortlinkDOList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(shortlinkDOList, ShortlinkGroupCountQueryRespDTO.class);
    }

    @SneakyThrows
    @Override
    public void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) {
        // 短链接接口的并发量有多少？如何测试？详情查看：https://tomgrx.com/shortlink/question
        // 面试中如何回答短链接是如何跳转长链接？详情查看：https://tomgrx.com/shortlink/question
        String serverName = request.getServerName();
        String serverPort = Optional.of(request.getServerPort())
                .filter(each -> !Objects.equals(each, 80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse("");
        String fullShortUrl = serverName + serverPort + "/" + shortUri;
        String originalLink = stringRedisTemplate.opsForValue().get(GOTO_KEY + fullShortUrl);
        if (StrUtil.isNotBlank(originalLink)) {
            shortlinkStats(buildLinkStatsRecordAndSetUser(fullShortUrl, request, response));
            ((HttpServletResponse) response).sendRedirect(originalLink);
            return;
        }
        boolean contains = shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl);
        if (!contains) {
            ((HttpServletResponse) response).sendRedirect("/page/notfound");
            return;
        }
        String gotoIsNullShortlink = stringRedisTemplate.opsForValue().get(GOTO_IS_NULL_KEY + fullShortUrl);
        if (StrUtil.isNotBlank(gotoIsNullShortlink)) {
            ((HttpServletResponse) response).sendRedirect("/page/notfound");
            return;
        }
        RLock lock = redissonClient.getLock(LOCK_GOTO_SHORT_LINK_KEY + fullShortUrl);
        lock.lock();
        try {
            originalLink = stringRedisTemplate.opsForValue().get(GOTO_KEY + fullShortUrl);
            if (StrUtil.isNotBlank(originalLink)) {
                shortlinkStats(buildLinkStatsRecordAndSetUser(fullShortUrl, request, response));
                ((HttpServletResponse) response).sendRedirect(originalLink);
                return;
            }
            gotoIsNullShortlink = stringRedisTemplate.opsForValue().get(GOTO_IS_NULL_KEY + fullShortUrl);
            if (StrUtil.isNotBlank(gotoIsNullShortlink)) {
                ((HttpServletResponse) response).sendRedirect("/page/notfound");
                return;
            }
            LambdaQueryWrapper<ShortlinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(ShortlinkGotoDO.class)
                    .eq(ShortlinkGotoDO::getFullShortUrl, fullShortUrl);
            ShortlinkGotoDO shortlinkGotoDO = shortlinkGotoMapper.selectOne(linkGotoQueryWrapper);
            if (shortlinkGotoDO == null) {
                stringRedisTemplate.opsForValue().set(GOTO_IS_NULL_KEY + fullShortUrl, "-", 30, TimeUnit.MINUTES);
                ((HttpServletResponse) response).sendRedirect("/page/notfound");
                return;
            }
            LambdaQueryWrapper<ShortlinkDO> queryWrapper = Wrappers.lambdaQuery(ShortlinkDO.class)
                    .eq(ShortlinkDO::getGid, shortlinkGotoDO.getGid())
                    .eq(ShortlinkDO::getFullShortUrl, fullShortUrl)
                    .eq(ShortlinkDO::getDelFlag, 0)
                    .eq(ShortlinkDO::getEnableStatus, 0);
            ShortlinkDO shortlinkDO = baseMapper.selectOne(queryWrapper);
            if (shortlinkDO == null || (shortlinkDO.getValidDate() != null && shortlinkDO.getValidDate().before(new Date()))) {
                stringRedisTemplate.opsForValue().set(GOTO_IS_NULL_KEY + fullShortUrl, "-", 30, TimeUnit.MINUTES);
                ((HttpServletResponse) response).sendRedirect("/page/notfound");
                return;
            }
            stringRedisTemplate.opsForValue().set(
                    GOTO_KEY + fullShortUrl,
                    shortlinkDO.getOriginUrl(),
                    LinkUtil.getLinkCacheValidTime(shortlinkDO.getValidDate()), TimeUnit.MILLISECONDS
            );
            shortlinkStats(buildLinkStatsRecordAndSetUser(fullShortUrl, request, response));
            ((HttpServletResponse) response).sendRedirect(shortlinkDO.getOriginUrl());
        } finally {
            lock.unlock();
        }
    }

    private ShortlinkStatsRecordDTO buildLinkStatsRecordAndSetUser(String fullShortUrl, ServletRequest request, ServletResponse response) {
        AtomicBoolean uvFirstFlag = new AtomicBoolean();
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        AtomicReference<String> uv = new AtomicReference<>();
        Runnable addResponseCookieTask = () -> {
            uv.set(UUID.fastUUID().toString());
            Cookie uvCookie = new Cookie("uv", uv.get());
            uvCookie.setMaxAge(60 * 60 * 24 * 30);
            uvCookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
            ((HttpServletResponse) response).addCookie(uvCookie);
            uvFirstFlag.set(Boolean.TRUE);
            stringRedisTemplate.opsForSet().add(UV_STATS_KEY + fullShortUrl, uv.get());
        };
        if (ArrayUtil.isNotEmpty(cookies)) {
            Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "uv"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresentOrElse(each -> {
                        uv.set(each);
                        Long uvAdded = stringRedisTemplate.opsForSet().add(UV_STATS_KEY + fullShortUrl, each);
                        uvFirstFlag.set(uvAdded != null && uvAdded > 0L);
                    }, addResponseCookieTask);
        } else {
            addResponseCookieTask.run();
        }
        String remoteAddr = LinkUtil.getActualIp(((HttpServletRequest) request));
        String os = LinkUtil.getOs(((HttpServletRequest) request));
        String browser = LinkUtil.getBrowser(((HttpServletRequest) request));
        String device = LinkUtil.getDevice(((HttpServletRequest) request));
        String network = LinkUtil.getNetwork(((HttpServletRequest) request));
        Long uipAdded = stringRedisTemplate.opsForSet().add(UIP_STATS_KEY + fullShortUrl, remoteAddr);
        boolean uipFirstFlag = uipAdded != null && uipAdded > 0L;
        return ShortlinkStatsRecordDTO.builder()
                .fullShortUrl(fullShortUrl)
                .uv(uv.get())
                .uvFirstFlag(uvFirstFlag.get())
                .uipFirstFlag(uipFirstFlag)
                .remoteAddr(remoteAddr)
                .os(os)
                .browser(browser)
                .device(device)
                .network(network)
                .currentDate(new Date())
                .build();
    }

    @Override
    public void shortlinkStats(ShortlinkStatsRecordDTO statsRecord) {
        Map<String, String> producerMap = new HashMap<>();
        producerMap.put("statsRecord", JSON.toJSONString(statsRecord));
        // 消息队列为什么选用RocketMQ？详情查看：https://tomgrx.com/shortlink/question
        shortlinkStatsSaveProducer.send(producerMap);
    }

    private String generateSuffix(ShortlinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String shorUri;
        while (true) {
            if (customGenerateCount > 10) {
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += UUID.randomUUID().toString();
            // 短链接哈希算法生成冲突问题如何解决？详情查看：https://tomgrx.com/shortlink/question
            shorUri = HashUtil.hashToBase62(originUrl);
            // 判断短链接是否存在为什么不使用Set结构？详情查看：https://tomgrx.com/shortlink/question
            // 如果布隆过滤器挂了，里边存的数据全丢失了，怎么恢复呢？详情查看：https://tomgrx.com/shortlink/question
            if (!shortUriCreateCachePenetrationBloomFilter.contains(createShortlinkDefaultDomain + "/" + shorUri)) {
                break;
            }
            customGenerateCount++;
        }
        return shorUri;
    }

    private String generateSuffixByLock(ShortlinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String shorUri;
        while (true) {
            if (customGenerateCount > 10) {
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += UUID.randomUUID().toString();
            // 短链接哈希算法生成冲突问题如何解决？详情查看：https://tomgrx.com/shortlink/question
            shorUri = HashUtil.hashToBase62(originUrl);
            LambdaQueryWrapper<ShortlinkDO> queryWrapper = Wrappers.lambdaQuery(ShortlinkDO.class)
                    .eq(ShortlinkDO::getGid, requestParam.getGid())
                    .eq(ShortlinkDO::getFullShortUrl, createShortlinkDefaultDomain + "/" + shorUri)
                    .eq(ShortlinkDO::getDelFlag, 0);
            ShortlinkDO shortlinkDO = baseMapper.selectOne(queryWrapper);
            if (shortlinkDO == null) {
                break;
            }
            customGenerateCount++;
        }
        return shorUri;
    }

    @SneakyThrows
    private String getFavicon(String url) {
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode) {
            Document document = Jsoup.connect(url).get();
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null) {
                return faviconLink.attr("abs:href");
            }
        }
        return null;
    }

    private void verificationWhitelist(String originUrl) {
        Boolean enable = gotoDomainWhiteListConfiguration.getEnable();
        if (enable == null || !enable) {
            return;
        }
        String domain = LinkUtil.extractDomain(originUrl);
        if (StrUtil.isBlank(domain)) {
            throw new ClientException("跳转链接填写错误");
        }
        List<String> details = gotoDomainWhiteListConfiguration.getDetails();
        if (!details.contains(domain)) {
            throw new ClientException("演示环境为避免恶意攻击，请生成以下网站跳转链接：" + gotoDomainWhiteListConfiguration.getNames());
        }
    }
}
