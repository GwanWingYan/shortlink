package com.tomgrx.shortlink.admin.remote.dto.resp;

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
public class ShortlinkStatsRespDTO {

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
     * 基础访问详情
     */
    private List<ShortlinkStatsAccessDailyRespDTO> daily;

    /**
     * 地区访问详情（仅国内）
     */
    private List<ShortlinkStatsLocaleCNRespDTO> localeCnStats;

    /**
     * 小时访问详情
     */
    private List<Integer> hourStats;

    /**
     * 高频访问IP详情
     */
    private List<ShortlinkStatsTopIpRespDTO> topIpStats;

    /**
     * 一周访问详情
     */
    private List<Integer> weekdayStats;

    /**
     * 浏览器访问详情
     */
    private List<ShortlinkStatsBrowserRespDTO> browserStats;

    /**
     * 操作系统访问详情
     */
    private List<ShortlinkStatsOsRespDTO> osStats;

    /**
     * 访客访问类型详情
     */
    private List<ShortlinkStatsUvRespDTO> uvTypeStats;

    /**
     * 访问设备类型详情
     */
    private List<ShortlinkStatsDeviceRespDTO> deviceStats;

    /**
     * 访问网络类型详情
     */
    private List<ShortlinkStatsNetworkRespDTO> networkStats;
}

