package com.tomgrx.shortlink.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.core.common.biz.user.UserContext;
import com.tomgrx.shortlink.core.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.core.dao.entity.*;
import com.tomgrx.shortlink.core.dao.mapper.*;
import com.tomgrx.shortlink.core.dto.biz.StatsRecordDTO;
import com.tomgrx.shortlink.core.dto.req.GroupAccessRecordReqDTO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkAccessRecordReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import com.tomgrx.shortlink.core.dto.resp.*;
import com.tomgrx.shortlink.core.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.LOCK_SAVE_STATS_KEY_PREFIX;
import static com.tomgrx.shortlink.constant.ShortlinkConstant.AMAP_REMOTE_URL;

/**
 * 短链接监控接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final RedissonClient redissonClient;
    private final ShortlinkMapper shortlinkMapper;
    private final GotoMapper gotoMapper;
    private final GroupMapper groupMapper;
    private final AccessStatsMapper accessStatsMapper;
    private final LocaleStatsMapper localeStatsMapper;
    private final OsStatsMapper osStatsMapper;
    private final BrowserStatsMapper browserStatsMapper;
    private final AccessLogMapper accessLogMapper;
    private final DeviceStatsMapper deviceStatsMapper;
    private final NetworkStatsMapper networkStatsMapper;
    private final TodayStatsMapper todayStatsMapper;

    @Value("${shortlink.stats.locale.amap-key}")
    private String statsLocaleAmapKey;

    /**
     * 获取单个短链接监控数据
     */
    @Override
    public StatsRespDTO getShortlinkStats(ShortlinkStatsReqDTO requestParam) {
        // 确保当前用户拥有分组
        checkGroupBelongToUser(requestParam.getGid());

        // 获取指定日期内每天的 PV, UV, UIP 数据
        List<AccessStatsDO> dailyAccessStatsList = accessStatsMapper.listDailyAccessStatsByShortlink(requestParam);
        if (CollUtil.isEmpty(dailyAccessStatsList)) {
            return null;
        }
        List<DailyAccessRespDTO> dailyStats = getDailyStats(dailyAccessStatsList, requestParam.getStartDate(), requestParam.getEndDate());

        // 获取指定日期内总体的 PV、UV、UIP 数据
        AccessStatsDO totalAccessStats = accessStatsMapper.getTotalAccessStatsByShortlink(requestParam);

        // 获取指定日期内在国内各个地区的访问量数据
        List<LocaleStatsDO> localeStatsList = localeStatsMapper.listLocaleByShortlink(requestParam);
        List<LocaleCNRespDTO> localeCnStats = getLocaleCnStats(localeStatsList);

        // 获取每个小时的 PV 数据
        List<AccessStatsDO> hourlyAccessStatsList = accessStatsMapper.listHourStatsByShortlink(requestParam);
        List<Integer> hourlyStats = getHourlyStats(hourlyAccessStatsList);

        // 获取高频访问 IP
        List<HashMap<String, Object>> top5IpList = accessLogMapper.listTop5IpByShortlink(requestParam);
        List<TopIpStatsRespDTO> topIpStats = getTopIpStats(top5IpList);

        // 获取一周每天的 PV 数据
        List<AccessStatsDO> weekdayAccessStatsList = accessStatsMapper.listWeekdayStatsByShortlink(requestParam);
        List<Integer> weekdayStats = getWeekdayStats(weekdayAccessStatsList);

        // 获取各种浏览器的访问量数据
        List<HashMap<String, Object>> browserStatsList = browserStatsMapper.listBrowserStatsByShortlink(requestParam);
        List<BrowserStatsRespDTO> browserStats = getBrowserStats(browserStatsList);

        // 获取各种操作系统的访问量数据
        List<HashMap<String, Object>> osStatsList = osStatsMapper.listOsStatsByShortlink(requestParam);
        List<OsStatsRespDTO> osStats = getOsStats(osStatsList);

        // 获取访客访问类型（新访客 or 旧访客）及访问量数据
        HashMap<String, Object> visitorTypeStatsList = accessLogMapper.findVisitorTypeCntByShortlink(requestParam);
        List<VisitorTypeStatsRespDTO> visitorTypeStats = getVisitorTypeStats(visitorTypeStatsList);

        // 获取各种访问设备的访问量数据
        List<DeviceStatsDO> deviceStatsList = deviceStatsMapper.listDeviceStatsByShortlink(requestParam);
        List<DeviceStatsRespDTO> deviceStats = getDeviceStats(deviceStatsList);

        // 访问网络类型详情
        List<NetworkStatsDO> networkStatsList = networkStatsMapper.listNetworkStatsByShortlink(requestParam);
        List<NetworkStatsRespDTO> networkStats = getNetworkStats(networkStatsList);

        return StatsRespDTO.builder()
                .pv(totalAccessStats.getPv())
                .uv(totalAccessStats.getUv())
                .uip(totalAccessStats.getUip())
                .dailyStats(dailyStats)
                .localeCnStats(localeCnStats)
                .hourStats(hourlyStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .osStats(osStats)
                .visitorTypeStats(visitorTypeStats)
                .deviceStats(deviceStats)
                .networkStats(networkStats)
                .build();
    }

    /**
     * 访问分组指定时间内监控数据
     */
    @Override
    public StatsRespDTO getGroupStats(GroupStatsReqDTO requestParam) {
        // 确保当前用户拥有分组
        checkGroupBelongToUser(requestParam.getGid());

        // 获取指定日期内短链接每天的 PV, UV, UIP 数据
        List<AccessStatsDO> dailyAccessStatsList = accessStatsMapper.listDailyAccessStatsByGroup(requestParam);
        if (CollUtil.isEmpty(dailyAccessStatsList)) {
            return null;
        }
        List<DailyAccessRespDTO> dailyStats = getDailyStats(dailyAccessStatsList, requestParam.getStartDate(), requestParam.getEndDate());

        // 获取指定日期内短链接总体的 PV、UV、UIP 数据
        AccessStatsDO totalAccessStats = accessLogMapper.getTotalAccessStatsByGroup(requestParam);

        // 获取指定日期内短链接在国内各个地区的访问量数据
        List<LocaleStatsDO> localeStatsList = localeStatsMapper.listLocaleByGroup(requestParam);
        List<LocaleCNRespDTO> localeCnStats = getLocaleCnStats(localeStatsList);

        // 获取每个小时短链接的 PV 数据
        List<AccessStatsDO> hourlyAccessStatsList = accessStatsMapper.listHourStatsByGroup(requestParam);
        List<Integer> hourlyStats = getHourlyStats(hourlyAccessStatsList);

        // 获取高频访问 IP
        List<HashMap<String, Object>> top5IpList = accessLogMapper.listTop5IpByGroup(requestParam);
        List<TopIpStatsRespDTO> topIpStats = getTopIpStats(top5IpList);

        // 获取一周每天的 PV 数据
        List<AccessStatsDO> weekdayAccessStatsList = accessStatsMapper.listWeekdayStatsByGroup(requestParam);
        List<Integer> weekdayStats = getWeekdayStats(weekdayAccessStatsList);

        // 获取各种浏览器的访问量数据
        List<HashMap<String, Object>> browserStatsList = browserStatsMapper.listBrowserStatsByGroup(requestParam);
        List<BrowserStatsRespDTO> browserStats = getBrowserStats(browserStatsList);

        // 获取各种操作系统的访问量数据
        List<HashMap<String, Object>> osStatsList = osStatsMapper.listOsStatsByGroup(requestParam);
        List<OsStatsRespDTO> osStats = getOsStats(osStatsList);

        // 获取各种访问设备的访问量数据
        List<DeviceStatsDO> deviceStatsList = deviceStatsMapper.listDeviceStatsByGroup(requestParam);
        List<DeviceStatsRespDTO> deviceStats = getDeviceStats(deviceStatsList);

        // 访问网络类型详情
        List<NetworkStatsDO> networkStatsList = networkStatsMapper.listNetworkStatsByGroup(requestParam);
        List<NetworkStatsRespDTO> networkStats = getNetworkStats(networkStatsList);

        return StatsRespDTO.builder()
                .pv(totalAccessStats.getPv())
                .uv(totalAccessStats.getUv())
                .uip(totalAccessStats.getUip())
                .dailyStats(dailyStats)
                .localeCnStats(localeCnStats)
                .hourStats(hourlyStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .osStats(osStats)
                .deviceStats(deviceStats)
                .networkStats(networkStats)
                .build();
    }

    /**
     * 访问单个短链接指定时间内访问记录
     */
    @Override
    public IPage<AccessRecordRespDTO> getShortlinkAccessRecord(ShortlinkAccessRecordReqDTO requestParam) {
        // 确保当前用户拥有分组
        checkGroupBelongToUser(requestParam.getGid());

        // 如果没有访问记录则直接返回
        LambdaQueryWrapper<AccessLogDO> queryWrapper = Wrappers.lambdaQuery(AccessLogDO.class)
                .eq(AccessLogDO::getLid, requestParam.getLid())
                .between(AccessLogDO::getCreateTime, requestParam.getStartDate(), requestParam.getEndDate())
                .eq(AccessLogDO::getDeleteFlag, 0)
                .orderByDesc(AccessLogDO::getCreateTime);
        IPage<AccessLogDO> accessLogDOIPage = accessLogMapper.selectPage(requestParam, queryWrapper);
        if (CollUtil.isEmpty(accessLogDOIPage.getRecords())) {
            return new Page<>();
        }

        // 设置访客类型
        IPage<AccessRecordRespDTO> result = accessLogDOIPage.convert(each -> BeanUtil.toBean(each, AccessRecordRespDTO.class));
        List<String> vidList = result.getRecords().stream()
                .map(AccessRecordRespDTO::getVid)
                .toList();
        List<Map<String, Object>> visitorTypeList = accessLogMapper.selectShortlinkVisitorTypeByVids(
                requestParam.getGid(),
                requestParam.getLid(),
                requestParam.getEnableFlag(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                vidList
        );
        result.getRecords().forEach(each -> {
            String visitorType = visitorTypeList.stream()
                    .filter(item -> Objects.equals(each.getVid(), item.get("vid")))
                    .findFirst()
                    .map(item -> item.get("visitorType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setVisitorType(visitorType);
        });
        return result;
    }

    /**
     * 访问分组指定时间内访问记录
     */
    @Override
    public IPage<AccessRecordRespDTO> getGroupAccessRecord(GroupAccessRecordReqDTO requestParam) {
        // 确保当前用户拥有分组
        checkGroupBelongToUser(requestParam.getGid());

        // 如果没有访问记录则直接返回
        IPage<AccessLogDO> accessLogDOIPage = accessLogMapper.selectGroupPage(requestParam);
        if (CollUtil.isEmpty(accessLogDOIPage.getRecords())) {
            return new Page<>();
        }

        // 设置访客类型
        IPage<AccessRecordRespDTO> result = accessLogDOIPage
                .convert(each -> BeanUtil.toBean(each, AccessRecordRespDTO.class));
        List<String> vidList = result.getRecords().stream()
                .map(AccessRecordRespDTO::getVid)
                .toList();
        List<Map<String, Object>> visitorTypeList = accessLogMapper.selectGroupVisitorTypeByVids(
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                vidList
        );
        result.getRecords().forEach(each -> {
            String visitorType = visitorTypeList.stream()
                    .filter(item -> Objects.equals(each.getVid(), item.get("vid")))
                    .findFirst()
                    .map(item -> item.get("visitorType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setVisitorType(visitorType);
        });
        return result;
    }

    /**
     * 保存监控记录
     */
    @Override
    public void saveStatsRecord(StatsRecordDTO statsRecord) {
        // 使用读写锁，既避免并发冲突，又提高读写性能
        String lid = statsRecord.getLid();
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(LOCK_SAVE_STATS_KEY_PREFIX + lid);
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        try {
            // 创建或更新基础监控数据
            LambdaQueryWrapper<GotoDO> queryWrapper = Wrappers.lambdaQuery(GotoDO.class)
                    .eq(GotoDO::getLid, lid);
            String gid = gotoMapper.selectOne(queryWrapper).getGid();
            Date currentDate = statsRecord.getCurrentDate();
            int hour = DateUtil.hour(currentDate, true);
            int weekday = DateUtil.dayOfWeekEnum(currentDate).getIso8601Value();
            AccessStatsDO accessStatsDO = AccessStatsDO.builder()
                    .pv(1)
                    .uv(statsRecord.getIsVisitorFirstVisit() ? 1 : 0)
                    .uip(statsRecord.getIsIpFirstVisit() ? 1 : 0)
                    .hour(hour)
                    .weekday(weekday)
                    .lid(lid)
                    .date(currentDate)
                    .build();
            accessStatsMapper.saveAccessStats(accessStatsDO);

            // 创建或更新地区监控数据
            Map<String, Object> localeParamMap = new HashMap<>();
            localeParamMap.put("key", statsLocaleAmapKey);
            localeParamMap.put("ip", statsRecord.getVip());
            String localeResultStr = HttpUtil.get(AMAP_REMOTE_URL, localeParamMap);
            JSONObject localeResultObj = JSON.parseObject(localeResultStr);
            String infoCode = localeResultObj.getString("infocode");
            String actualProvince = "未知";
            String actualCity = "未知";
            if (StrUtil.isNotBlank(infoCode) && StrUtil.equals(infoCode, "10000")) {
                String province = localeResultObj.getString("province");
                boolean unknownFlag = StrUtil.equals(province, "[]");
                LocaleStatsDO localeStatsDO = LocaleStatsDO.builder()
                        .province(actualProvince = unknownFlag ? actualProvince : province)
                        .city(actualCity = unknownFlag ? actualCity : localeResultObj.getString("city"))
                        .adcode(unknownFlag ? "未知" : localeResultObj.getString("adcode"))
                        .cnt(1)
                        .lid(lid)
                        .country("中国")
                        .date(currentDate)
                        .build();
                localeStatsMapper.saveLocaleStats(localeStatsDO);
            }

            // 创建或更新操作系统监控数据
            OsStatsDO osStatsDO = OsStatsDO.builder()
                    .os(statsRecord.getOs())
                    .cnt(1)
                    .lid(lid)
                    .date(currentDate)
                    .build();
            osStatsMapper.saveOsStats(osStatsDO);

            // 创建或更新浏览器监控数据
            BrowserStatsDO browserStatsDO = BrowserStatsDO.builder()
                    .browser(statsRecord.getBrowser())
                    .cnt(1)
                    .lid(lid)
                    .date(currentDate)
                    .build();
            browserStatsMapper.saveBrowserStats(browserStatsDO);

            // 创建或更新设备监控数据
            DeviceStatsDO deviceStatsDO = DeviceStatsDO.builder()
                    .device(statsRecord.getDevice())
                    .cnt(1)
                    .lid(lid)
                    .date(currentDate)
                    .build();
            deviceStatsMapper.saveDeviceStats(deviceStatsDO);

            // 创建或更新网络监控数据
            NetworkStatsDO networkStatsDO = NetworkStatsDO.builder()
                    .network(statsRecord.getNetwork())
                    .cnt(1)
                    .lid(lid)
                    .date(currentDate)
                    .build();
            networkStatsMapper.saveNetworkStats(networkStatsDO);

            // 创建访问记录监控数据
            AccessLogDO accessLogDO = AccessLogDO.builder()
                    .vid(statsRecord.getVid())
                    .ip(statsRecord.getVip())
                    .browser(statsRecord.getBrowser())
                    .os(statsRecord.getOs())
                    .network(statsRecord.getNetwork())
                    .device(statsRecord.getDevice())
                    .locale(StrUtil.join("-", "中国", actualProvince, actualCity))
                    .lid(lid)
                    .build();
            accessLogMapper.insert(accessLogDO);

            // 更新短链接监控数据
            shortlinkMapper.incrementStats(
                    gid,
                    lid,
                    1,
                    statsRecord.getIsVisitorFirstVisit() ? 1 : 0,
                    statsRecord.getIsIpFirstVisit() ? 1 : 0);

            // 创建或更新今日统计数据
            TodayStatsDO todayStatsDO = TodayStatsDO.builder()
                    .todayPv(1)
                    .todayUv(statsRecord.getIsVisitorFirstVisit() ? 1 : 0)
                    .todayUip(statsRecord.getIsIpFirstVisit() ? 1 : 0)
                    .lid(lid)
                    .date(currentDate)
                    .build();
            todayStatsMapper.saveTodayStats(todayStatsDO);
        } catch (Throwable ex) {
            log.error("短链接访问量统计异常", ex);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 确保当前用户拥有分组
     */
    public void checkGroupBelongToUser(String gid) throws ServiceException {
        // 拒绝未登录用户
        String userName = Optional.ofNullable(UserContext.getUserName())
                .orElseThrow(() -> new ServiceException("用户未登录"));
        // 拒绝不属于当前用户的分组
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUserName, userName);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("用户信息与分组标识不匹配");
        }
    }

    /**
     * 获取指定日期内每天的 PV, UV, UIP 数据
     * @param param 每个元素的 PV, UV, 或 UIP 至少一项不是 0
     * @param startDate 开始日期
     * @param endDate 结束日期（含）
     * @return 从 startDate 到 endDate 每一天的 PV, UV 和 UIP 监控数据。如果某天这三项数据都是 0，这天的数据也存在于返回结果中。
     */
    private List<DailyAccessRespDTO> getDailyStats(List<AccessStatsDO> param, String startDate, String endDate) {
        List<DailyAccessRespDTO> result = new ArrayList<>();
        List<String> dateRange = DateUtil.rangeToList(
                        DateUtil.parse(startDate),
                        DateUtil.parse(endDate),
                        DateField.DAY_OF_MONTH)
                .stream()
                .map(DateUtil::formatDate)
                .toList();
        dateRange.forEach(each -> param.stream()
                .filter(item -> Objects.equals(each, DateUtil.formatDate(item.getDate())))
                .findFirst()
                .ifPresentOrElse(item -> {
                    DailyAccessRespDTO dailyAccessRespDTO = DailyAccessRespDTO.builder()
                            .date(each)
                            .pv(item.getPv())
                            .uv(item.getUv())
                            .uip(item.getUip())
                            .build();
                    result.add(dailyAccessRespDTO);
                }, () -> {
                    DailyAccessRespDTO dailyAccessRespDTO = DailyAccessRespDTO.builder()
                            .date(each)
                            .pv(0)
                            .uv(0)
                            .uip(0)
                            .build();
                    result.add(dailyAccessRespDTO);
                }));
        return result;
    }

    /**
     * 获取指定日期内在国内各个地区的访问量数据，并添加各个省份的占比数据
     */
    private List<LocaleCNRespDTO> getLocaleCnStats(List<LocaleStatsDO> param) {
        List<LocaleCNRespDTO> result = new ArrayList<>();
        int localeCnSum = param.stream()
                .mapToInt(LocaleStatsDO::getCnt)
                .sum();
        param.forEach(each -> {
            double ratio = (double) each.getCnt() / localeCnSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            LocaleCNRespDTO localeCNRespDTO = LocaleCNRespDTO.builder()
                    .cnt(each.getCnt())
                    .locale(each.getProvince())
                    .ratio(actualRatio)
                    .build();
            result.add(localeCNRespDTO);
        });
        return result;
    }

    /**
     * 获取每个小时的 PV 数据
     */
    private List<Integer> getHourlyStats(List<AccessStatsDO> param) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            AtomicInteger hour = new AtomicInteger(i);
            int hourCnt = param.stream()
                    .filter(each -> Objects.equals(each.getHour(), hour.get()))
                    .findFirst()
                    .map(AccessStatsDO::getPv)
                    .orElse(0);
            result.add(hourCnt);
        }
        return result;
    }

    /**
     * 获取高频访问 IP 数据
     */
    private List<TopIpStatsRespDTO> getTopIpStats(List<HashMap<String, Object>> param) {
        List<TopIpStatsRespDTO> topIpStats = new ArrayList<>();
        param.forEach(each -> {
            TopIpStatsRespDTO statsTopIpRespDTO = TopIpStatsRespDTO.builder()
                    .ip(each.get("ip").toString())
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .build();
            topIpStats.add(statsTopIpRespDTO);
        });
        return topIpStats;
    }

    /**
     * 获取一周每天的 PV 数据
     */
    private List<Integer> getWeekdayStats(List<AccessStatsDO> param) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            AtomicInteger weekday = new AtomicInteger(i);
            int weekdayCnt = param.stream()
                    .filter(each -> Objects.equals(each.getWeekday(), weekday.get()))
                    .findFirst()
                    .map(AccessStatsDO::getPv)
                    .orElse(0);
            result.add(weekdayCnt);
        }
        return result;
    }

    /**
     * 获取各种浏览器的访问量数据
     */
    private List<BrowserStatsRespDTO> getBrowserStats(List<HashMap<String, Object>> param) {
        List<BrowserStatsRespDTO> result = new ArrayList<>();
        int browserSum = param.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        param.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / browserSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            BrowserStatsRespDTO browserStatsRespDTO = BrowserStatsRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .browser(each.get("browser").toString())
                    .ratio(actualRatio)
                    .build();
            result.add(browserStatsRespDTO);
        });
        return result;
    }

    /**
     * 获取各种操作系统的访问量数据
     */
    private List<OsStatsRespDTO> getOsStats(List<HashMap<String, Object>> param) {
        List<OsStatsRespDTO> result = new ArrayList<>();
        int osSum = param.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        param.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / osSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            OsStatsRespDTO osRespDTO = OsStatsRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .os(each.get("os").toString())
                    .ratio(actualRatio)
                    .build();
            result.add(osRespDTO);
        });
        return result;
    }

    /**
     * 获取访客访问类型（新访客 or 旧访客）及访问量数据
     */
    private List<VisitorTypeStatsRespDTO> getVisitorTypeStats(HashMap<String, Object> param) {
        List<VisitorTypeStatsRespDTO> visitorTypeStats = new ArrayList<>();
        int oldVisitorCnt = Integer.parseInt(
                Optional.ofNullable(param)
                        .map(each -> each.get("oldVisitorCnt"))
                        .map(Object::toString)
                        .orElse("0")
        );
        int newVisitorCnt = Integer.parseInt(
                Optional.ofNullable(param)
                        .map(each -> each.get("newVisitorCnt"))
                        .map(Object::toString)
                        .orElse("0")
        );
        int visitorSum = oldVisitorCnt + newVisitorCnt;
        double oldRatio = (double) oldVisitorCnt / visitorSum;
        double actualOldRatio = Math.round(oldRatio * 100.0) / 100.0;
        double newRatio = (double) newVisitorCnt / visitorSum;
        double actualNewRatio = Math.round(newRatio * 100.0) / 100.0;
        VisitorTypeStatsRespDTO oldVisitorRespDTO = VisitorTypeStatsRespDTO.builder()
                .visitorType("oldVisitor")
                .cnt(oldVisitorCnt)
                .ratio(actualOldRatio)
                .build();
        visitorTypeStats.add(oldVisitorRespDTO);
        VisitorTypeStatsRespDTO newVisitorRespDTO = VisitorTypeStatsRespDTO.builder()
                .visitorType("newVisitor")
                .cnt(newVisitorCnt)
                .ratio(actualNewRatio)
                .build();
        visitorTypeStats.add(newVisitorRespDTO);
        return visitorTypeStats;
    }

    /**
     * 获取各种访问设备的访问量数据
     */
    private List<DeviceStatsRespDTO> getDeviceStats(List<DeviceStatsDO> param) {
        List<DeviceStatsRespDTO> result = new ArrayList<>();
        int deviceSum = param.stream()
                .mapToInt(DeviceStatsDO::getCnt)
                .sum();
        param.forEach(each -> {
            double ratio = (double) each.getCnt() / deviceSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            DeviceStatsRespDTO deviceRespDTO = DeviceStatsRespDTO.builder()
                    .cnt(each.getCnt())
                    .device(each.getDevice())
                    .ratio(actualRatio)
                    .build();
            result.add(deviceRespDTO);
        });
        return result;
    }

    /**
     * 获取各种网络类型的访问量数据
     */
    private List<NetworkStatsRespDTO> getNetworkStats(List<NetworkStatsDO> param) {
        List<NetworkStatsRespDTO> result = new ArrayList<>();
        int networkSum = param.stream()
                .mapToInt(NetworkStatsDO::getCnt)
                .sum();
        param.forEach(each -> {
            double ratio = (double) each.getCnt() / networkSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            NetworkStatsRespDTO networkRespDTO = NetworkStatsRespDTO.builder()
                    .cnt(each.getCnt())
                    .network(each.getNetwork())
                    .ratio(actualRatio)
                    .build();
            result.add(networkRespDTO);
        });
        return result;
    }
}
