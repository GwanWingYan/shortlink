package com.tomgrx.shortlink.core.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 短链接监控返回参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsRespDTO {

    /**
     * 访问量
     */
    private Integer pv;

    /**
     * 独立访客数
     */
    private Integer uv;

    /**
     * 独立IP数
     */
    private Integer uip;

    /**
     * 每日访问监控统计数据
     */
    private List<DailyAccessRespDTO> dailyStats;

    /**
     * 地区访问详情（仅国内）
     */
    private List<LocaleCNRespDTO> localeCnStats;

    /**
     * 小时访问详情
     */
    private List<Integer> hourStats;

    /**
     * 高频访问IP详情
     */
    private List<TopIpStatsRespDTO> topIpStats;

    /**
     * 一周访问详情
     */
    private List<Integer> weekdayStats;

    /**
     * 浏览器访问详情
     */
    private List<BrowserStatsRespDTO> browserStats;

    /**
     * 操作系统访问详情
     */
    private List<OsStatsRespDTO> osStats;

    /**
     * 访客访问类型详情
     */
    private List<VisitorTypeStatsRespDTO> visitorTypeStats;

    /**
     * 访问设备类型详情
     */
    private List<DeviceStatsRespDTO> deviceStats;

    /**
     * 访问网络类型详情
     */
    private List<NetworkStatsRespDTO> networkStats;
}