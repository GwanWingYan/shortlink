package com.tomgrx.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.project.common.biz.user.UserContext;
import com.tomgrx.shortlink.project.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.project.dao.entity.*;
import com.tomgrx.shortlink.project.dao.mapper.*;
import com.tomgrx.shortlink.project.dto.req.ShortlinkGroupStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkGroupStatsReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkStatsReqDTO;
import com.tomgrx.shortlink.project.dto.resp.*;
import com.tomgrx.shortlink.project.service.ShortlinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 短链接监控接口实现层
 */
@Service
@RequiredArgsConstructor
public class ShortlinkStatsServiceImpl implements ShortlinkStatsService {

    private final LinkGroupMapper linkGroupMapper;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;

    @Override
    public ShortlinkStatsRespDTO oneShortlinkStats(ShortlinkStatsReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid());

        // 查询访问记录
        List<LinkAccessStatsDO> listStatsByShortlink = linkAccessStatsMapper.listStatsByShortlink(requestParam);
        if (CollUtil.isEmpty(listStatsByShortlink)) {
            return null;
        }

        // 基础访问数据
        LinkAccessStatsDO pvUvUidStatsByShortlink = linkAccessLogsMapper.findPvUvUidStatsByShortlink(requestParam);
        // 基础访问详情
        List<ShortlinkStatsAccessDailyRespDTO> daily = new ArrayList<>();
        List<String> rangeDates = DateUtil.rangeToList(DateUtil.parse(requestParam.getStartDate()), DateUtil.parse(requestParam.getEndDate()), DateField.DAY_OF_MONTH).stream()
                .map(DateUtil::formatDate)
                .toList();
        rangeDates.forEach(each -> listStatsByShortlink.stream()
                .filter(item -> Objects.equals(each, DateUtil.formatDate(item.getDate())))
                .findFirst()
                .ifPresentOrElse(item -> {
                    ShortlinkStatsAccessDailyRespDTO accessDailyRespDTO = ShortlinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(item.getPv())
                            .uv(item.getUv())
                            .uip(item.getUip())
                            .build();
                    daily.add(accessDailyRespDTO);
                }, () -> {
                    ShortlinkStatsAccessDailyRespDTO accessDailyRespDTO = ShortlinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(0)
                            .uv(0)
                            .uip(0)
                            .build();
                    daily.add(accessDailyRespDTO);
                }));

        // 地区访问详情（仅国内）
        List<ShortlinkStatsLocaleCNRespDTO> localeCnStats = new ArrayList<>();
        List<LinkLocaleStatsDO> listedLocaleByShortlink = linkLocaleStatsMapper.listLocaleByShortlink(requestParam);
        int localeCnSum = listedLocaleByShortlink.stream()
                .mapToInt(LinkLocaleStatsDO::getCnt)
                .sum();
        listedLocaleByShortlink.forEach(each -> {
            double ratio = (double) each.getCnt() / localeCnSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsLocaleCNRespDTO localeCNRespDTO = ShortlinkStatsLocaleCNRespDTO.builder()
                    .cnt(each.getCnt())
                    .locale(each.getProvince())
                    .ratio(actualRatio)
                    .build();
            localeCnStats.add(localeCNRespDTO);
        });

        // 小时访问详情
        List<Integer> hourStats = new ArrayList<>();
        List<LinkAccessStatsDO> listHourStatsByShortlink = linkAccessStatsMapper.listHourStatsByShortlink(requestParam);
        for (int i = 0; i < 24; i++) {
            AtomicInteger hour = new AtomicInteger(i);
            int hourCnt = listHourStatsByShortlink.stream()
                    .filter(each -> Objects.equals(each.getHour(), hour.get()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            hourStats.add(hourCnt);
        }

        // 高频访问 IP 详情
        List<ShortlinkStatsTopIpRespDTO> topIpStats = new ArrayList<>();
        List<HashMap<String, Object>> listTopIpByShortlink = linkAccessLogsMapper.listTopIpByShortlink(requestParam);
        listTopIpByShortlink.forEach(each -> {
            ShortlinkStatsTopIpRespDTO statsTopIpRespDTO = ShortlinkStatsTopIpRespDTO.builder()
                    .ip(each.get("ip").toString())
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .build();
            topIpStats.add(statsTopIpRespDTO);
        });

        // 一周访问详情
        List<Integer> weekdayStats = new ArrayList<>();
        List<LinkAccessStatsDO> listWeekdayStatsByShortlink = linkAccessStatsMapper.listWeekdayStatsByShortlink(requestParam);
        for (int i = 1; i <= 7; i++) {
            AtomicInteger weekday = new AtomicInteger(i);
            int weekdayCnt = listWeekdayStatsByShortlink.stream()
                    .filter(each -> Objects.equals(each.getWeekday(), weekday.get()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            weekdayStats.add(weekdayCnt);
        }

        // 浏览器访问详情
        List<ShortlinkStatsBrowserRespDTO> browserStats = new ArrayList<>();
        List<HashMap<String, Object>> listBrowserStatsByShortlink = linkBrowserStatsMapper.listBrowserStatsByShortlink(requestParam);
        int browserSum = listBrowserStatsByShortlink.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listBrowserStatsByShortlink.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / browserSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsBrowserRespDTO browserRespDTO = ShortlinkStatsBrowserRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .browser(each.get("browser").toString())
                    .ratio(actualRatio)
                    .build();
            browserStats.add(browserRespDTO);
        });

        // 操作系统访问详情
        List<ShortlinkStatsOsRespDTO> osStats = new ArrayList<>();
        List<HashMap<String, Object>> listOsStatsByShortlink = linkOsStatsMapper.listOsStatsByShortlink(requestParam);
        int osSum = listOsStatsByShortlink.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listOsStatsByShortlink.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / osSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsOsRespDTO osRespDTO = ShortlinkStatsOsRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .os(each.get("os").toString())
                    .ratio(actualRatio)
                    .build();
            osStats.add(osRespDTO);
        });

        // 访客访问类型详情
        List<ShortlinkStatsUvRespDTO> uvTypeStats = new ArrayList<>();
        HashMap<String, Object> findUvTypeByShortlink = linkAccessLogsMapper.findUvTypeCntByShortlink(requestParam);
        int oldUserCnt = Integer.parseInt(
                Optional.ofNullable(findUvTypeByShortlink)
                        .map(each -> each.get("oldUserCnt"))
                        .map(Object::toString)
                        .orElse("0")
        );
        int newUserCnt = Integer.parseInt(
                Optional.ofNullable(findUvTypeByShortlink)
                        .map(each -> each.get("newUserCnt"))
                        .map(Object::toString)
                        .orElse("0")
        );
        int uvSum = oldUserCnt + newUserCnt;
        double oldRatio = (double) oldUserCnt / uvSum;
        double actualOldRatio = Math.round(oldRatio * 100.0) / 100.0;
        double newRatio = (double) newUserCnt / uvSum;
        double actualNewRatio = Math.round(newRatio * 100.0) / 100.0;
        ShortlinkStatsUvRespDTO oldUvRespDTO = ShortlinkStatsUvRespDTO.builder()
                .uvType("oldUser")
                .cnt(oldUserCnt)
                .ratio(actualOldRatio)
                .build();
        uvTypeStats.add(oldUvRespDTO);
        ShortlinkStatsUvRespDTO newUvRespDTO = ShortlinkStatsUvRespDTO.builder()
                .uvType("newUser")
                .cnt(newUserCnt)
                .ratio(actualNewRatio)
                .build();
        uvTypeStats.add(newUvRespDTO);

        // 访问设备类型详情
        List<ShortlinkStatsDeviceRespDTO> deviceStats = new ArrayList<>();
        List<LinkDeviceStatsDO> listDeviceStatsByShortlink = linkDeviceStatsMapper.listDeviceStatsByShortlink(requestParam);
        int deviceSum = listDeviceStatsByShortlink.stream()
                .mapToInt(LinkDeviceStatsDO::getCnt)
                .sum();
        listDeviceStatsByShortlink.forEach(each -> {
            double ratio = (double) each.getCnt() / deviceSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsDeviceRespDTO deviceRespDTO = ShortlinkStatsDeviceRespDTO.builder()
                    .cnt(each.getCnt())
                    .device(each.getDevice())
                    .ratio(actualRatio)
                    .build();
            deviceStats.add(deviceRespDTO);
        });

        // 访问网络类型详情
        List<ShortlinkStatsNetworkRespDTO> networkStats = new ArrayList<>();
        List<LinkNetworkStatsDO> listNetworkStatsByShortlink = linkNetworkStatsMapper.listNetworkStatsByShortlink(requestParam);
        int networkSum = listNetworkStatsByShortlink.stream()
                .mapToInt(LinkNetworkStatsDO::getCnt)
                .sum();
        listNetworkStatsByShortlink.forEach(each -> {
            double ratio = (double) each.getCnt() / networkSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsNetworkRespDTO networkRespDTO = ShortlinkStatsNetworkRespDTO.builder()
                    .cnt(each.getCnt())
                    .network(each.getNetwork())
                    .ratio(actualRatio)
                    .build();
            networkStats.add(networkRespDTO);
        });

        return ShortlinkStatsRespDTO.builder()
                .pv(pvUvUidStatsByShortlink.getPv())
                .uv(pvUvUidStatsByShortlink.getUv())
                .uip(pvUvUidStatsByShortlink.getUip())
                .daily(daily)
                .localeCnStats(localeCnStats)
                .hourStats(hourStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .osStats(osStats)
                .uvTypeStats(uvTypeStats)
                .deviceStats(deviceStats)
                .networkStats(networkStats)
                .build();
    }

    @Override
    public ShortlinkStatsRespDTO groupShortlinkStats(ShortlinkGroupStatsReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid());
        List<LinkAccessStatsDO> listStatsByGroup = linkAccessStatsMapper.listStatsByGroup(requestParam);
        if (CollUtil.isEmpty(listStatsByGroup)) {
            return null;
        }
        // 基础访问数据
        LinkAccessStatsDO pvUvUidStatsByGroup = linkAccessLogsMapper.findPvUvUidStatsByGroup(requestParam);
        // 基础访问详情
        List<ShortlinkStatsAccessDailyRespDTO> daily = new ArrayList<>();
        List<String> rangeDates = DateUtil.rangeToList(DateUtil.parse(requestParam.getStartDate()), DateUtil.parse(requestParam.getEndDate()), DateField.DAY_OF_MONTH).stream()
                .map(DateUtil::formatDate)
                .toList();
        rangeDates.forEach(each -> listStatsByGroup.stream()
                .filter(item -> Objects.equals(each, DateUtil.formatDate(item.getDate())))
                .findFirst()
                .ifPresentOrElse(item -> {
                    ShortlinkStatsAccessDailyRespDTO accessDailyRespDTO = ShortlinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(item.getPv())
                            .uv(item.getUv())
                            .uip(item.getUip())
                            .build();
                    daily.add(accessDailyRespDTO);
                }, () -> {
                    ShortlinkStatsAccessDailyRespDTO accessDailyRespDTO = ShortlinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(0)
                            .uv(0)
                            .uip(0)
                            .build();
                    daily.add(accessDailyRespDTO);
                }));
        // 地区访问详情（仅国内）
        List<ShortlinkStatsLocaleCNRespDTO> localeCnStats = new ArrayList<>();
        List<LinkLocaleStatsDO> listedLocaleByGroup = linkLocaleStatsMapper.listLocaleByGroup(requestParam);
        int localeCnSum = listedLocaleByGroup.stream()
                .mapToInt(LinkLocaleStatsDO::getCnt)
                .sum();
        listedLocaleByGroup.forEach(each -> {
            double ratio = (double) each.getCnt() / localeCnSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsLocaleCNRespDTO localeCNRespDTO = ShortlinkStatsLocaleCNRespDTO.builder()
                    .cnt(each.getCnt())
                    .locale(each.getProvince())
                    .ratio(actualRatio)
                    .build();
            localeCnStats.add(localeCNRespDTO);
        });
        // 小时访问详情
        List<Integer> hourStats = new ArrayList<>();
        List<LinkAccessStatsDO> listHourStatsByGroup = linkAccessStatsMapper.listHourStatsByGroup(requestParam);
        for (int i = 0; i < 24; i++) {
            AtomicInteger hour = new AtomicInteger(i);
            int hourCnt = listHourStatsByGroup.stream()
                    .filter(each -> Objects.equals(each.getHour(), hour.get()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            hourStats.add(hourCnt);
        }
        // 高频访问IP详情
        List<ShortlinkStatsTopIpRespDTO> topIpStats = new ArrayList<>();
        List<HashMap<String, Object>> listTopIpByGroup = linkAccessLogsMapper.listTopIpByGroup(requestParam);
        listTopIpByGroup.forEach(each -> {
            ShortlinkStatsTopIpRespDTO statsTopIpRespDTO = ShortlinkStatsTopIpRespDTO.builder()
                    .ip(each.get("ip").toString())
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .build();
            topIpStats.add(statsTopIpRespDTO);
        });
        // 一周访问详情
        List<Integer> weekdayStats = new ArrayList<>();
        List<LinkAccessStatsDO> listWeekdayStatsByGroup = linkAccessStatsMapper.listWeekdayStatsByGroup(requestParam);
        for (int i = 1; i < 8; i++) {
            AtomicInteger weekday = new AtomicInteger(i);
            int weekdayCnt = listWeekdayStatsByGroup.stream()
                    .filter(each -> Objects.equals(each.getWeekday(), weekday.get()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            weekdayStats.add(weekdayCnt);
        }
        // 浏览器访问详情
        List<ShortlinkStatsBrowserRespDTO> browserStats = new ArrayList<>();
        List<HashMap<String, Object>> listBrowserStatsByGroup = linkBrowserStatsMapper.listBrowserStatsByGroup(requestParam);
        int browserSum = listBrowserStatsByGroup.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listBrowserStatsByGroup.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / browserSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsBrowserRespDTO browserRespDTO = ShortlinkStatsBrowserRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .browser(each.get("browser").toString())
                    .ratio(actualRatio)
                    .build();
            browserStats.add(browserRespDTO);
        });
        // 操作系统访问详情
        List<ShortlinkStatsOsRespDTO> osStats = new ArrayList<>();
        List<HashMap<String, Object>> listOsStatsByGroup = linkOsStatsMapper.listOsStatsByGroup(requestParam);
        int osSum = listOsStatsByGroup.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listOsStatsByGroup.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / osSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsOsRespDTO osRespDTO = ShortlinkStatsOsRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .os(each.get("os").toString())
                    .ratio(actualRatio)
                    .build();
            osStats.add(osRespDTO);
        });
        // 访问设备类型详情
        List<ShortlinkStatsDeviceRespDTO> deviceStats = new ArrayList<>();
        List<LinkDeviceStatsDO> listDeviceStatsByGroup = linkDeviceStatsMapper.listDeviceStatsByGroup(requestParam);
        int deviceSum = listDeviceStatsByGroup.stream()
                .mapToInt(LinkDeviceStatsDO::getCnt)
                .sum();
        listDeviceStatsByGroup.forEach(each -> {
            double ratio = (double) each.getCnt() / deviceSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsDeviceRespDTO deviceRespDTO = ShortlinkStatsDeviceRespDTO.builder()
                    .cnt(each.getCnt())
                    .device(each.getDevice())
                    .ratio(actualRatio)
                    .build();
            deviceStats.add(deviceRespDTO);
        });
        // 访问网络类型详情
        List<ShortlinkStatsNetworkRespDTO> networkStats = new ArrayList<>();
        List<LinkNetworkStatsDO> listNetworkStatsByGroup = linkNetworkStatsMapper.listNetworkStatsByGroup(requestParam);
        int networkSum = listNetworkStatsByGroup.stream()
                .mapToInt(LinkNetworkStatsDO::getCnt)
                .sum();
        listNetworkStatsByGroup.forEach(each -> {
            double ratio = (double) each.getCnt() / networkSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortlinkStatsNetworkRespDTO networkRespDTO = ShortlinkStatsNetworkRespDTO.builder()
                    .cnt(each.getCnt())
                    .network(each.getNetwork())
                    .ratio(actualRatio)
                    .build();
            networkStats.add(networkRespDTO);
        });
        return ShortlinkStatsRespDTO.builder()
                .pv(pvUvUidStatsByGroup.getPv())
                .uv(pvUvUidStatsByGroup.getUv())
                .uip(pvUvUidStatsByGroup.getUip())
                .daily(daily)
                .localeCnStats(localeCnStats)
                .hourStats(hourStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .osStats(osStats)
                .deviceStats(deviceStats)
                .networkStats(networkStats)
                .build();
    }

    @Override
    public IPage<ShortlinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortlinkStatsAccessRecordReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid());

        LambdaQueryWrapper<LinkAccessLogsDO> queryWrapper = Wrappers.lambdaQuery(LinkAccessLogsDO.class)
                .eq(LinkAccessLogsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .between(LinkAccessLogsDO::getCreateTime, requestParam.getStartDate(), requestParam.getEndDate())
                .eq(LinkAccessLogsDO::getDelFlag, 0)
                .orderByDesc(LinkAccessLogsDO::getCreateTime);
        IPage<LinkAccessLogsDO> linkAccessLogsDOIPage = linkAccessLogsMapper.selectPage(requestParam, queryWrapper);
        if (CollUtil.isEmpty(linkAccessLogsDOIPage.getRecords())) {
            return new Page<>();
        }
        IPage<ShortlinkStatsAccessRecordRespDTO> actualResult = linkAccessLogsDOIPage.convert(each -> BeanUtil.toBean(each, ShortlinkStatsAccessRecordRespDTO.class));
        List<String> userAccessLogsList = actualResult.getRecords().stream()
                .map(ShortlinkStatsAccessRecordRespDTO::getUser)
                .toList();
        List<Map<String, Object>> uvTypeList = linkAccessLogsMapper.selectUvTypeByUsers(
                requestParam.getGid(),
                requestParam.getFullShortUrl(),
                requestParam.getEnableStatus(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                userAccessLogsList
        );
        actualResult.getRecords().forEach(each -> {
            String uvType = uvTypeList.stream()
                    .filter(item -> Objects.equals(each.getUser(), item.get("user")))
                    .findFirst()
                    .map(item -> item.get("UvType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setUvType(uvType);
        });
        return actualResult;
    }

    @Override
    public IPage<ShortlinkStatsAccessRecordRespDTO> groupShortlinkStatsAccessRecord(ShortlinkGroupStatsAccessRecordReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid());
        IPage<LinkAccessLogsDO> linkAccessLogsDOIPage = linkAccessLogsMapper.selectGroupPage(requestParam);
        if (CollUtil.isEmpty(linkAccessLogsDOIPage.getRecords())) {
            return new Page<>();
        }
        IPage<ShortlinkStatsAccessRecordRespDTO> actualResult = linkAccessLogsDOIPage
                .convert(each -> BeanUtil.toBean(each, ShortlinkStatsAccessRecordRespDTO.class));
        List<String> userAccessLogsList = actualResult.getRecords().stream()
                .map(ShortlinkStatsAccessRecordRespDTO::getUser)
                .toList();
        List<Map<String, Object>> uvTypeList = linkAccessLogsMapper.selectGroupUvTypeByUsers(
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                userAccessLogsList
        );
        actualResult.getRecords().forEach(each -> {
            String uvType = uvTypeList.stream()
                    .filter(item -> Objects.equals(each.getUser(), item.get("user")))
                    .findFirst()
                    .map(item -> item.get("uvType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setUvType(uvType);
        });
        return actualResult;
    }

    public void checkGroupBelongToUser(String gid) throws ServiceException {
        String userName = Optional.ofNullable(UserContext.getUserName())
                .orElseThrow(() -> new ServiceException("用户未登录"));
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUserName, userName);
        List<GroupDO> groupDOList = linkGroupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("用户信息与分组标识不匹配");
        }
    }
}
