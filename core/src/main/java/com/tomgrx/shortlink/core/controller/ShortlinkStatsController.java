package com.tomgrx.shortlink.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.core.common.convention.result.Result;
import com.tomgrx.shortlink.core.common.convention.result.Results;
import com.tomgrx.shortlink.core.dto.req.GroupAccessRecordReqDTO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkAccessRecordReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import com.tomgrx.shortlink.core.dto.resp.AccessRecordRespDTO;
import com.tomgrx.shortlink.core.dto.resp.StatsRespDTO;
import com.tomgrx.shortlink.core.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortlinkStatsController {

    private final StatsService statsService;

    /**
     * 访问短链接指定时间内监控数据
     */
    @GetMapping("/api/shortlink/v1/stats")
    public Result<StatsRespDTO> getShortlinkStats(ShortlinkStatsReqDTO requestParam) {
        return Results.success(statsService.getShortlinkStats(requestParam));
    }

    /**
     * 访问分组指定时间内监控数据
     */
    @GetMapping("/api/shortlink/v1/stats/group")
    public Result<StatsRespDTO> getGroupStats(GroupStatsReqDTO requestParam) {
        return Results.success(statsService.getGroupStats(requestParam));
    }

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/shortlink/v1/stats/access-record")
    public Result<IPage<AccessRecordRespDTO>> shortlinkStatsAccessRecord(ShortlinkAccessRecordReqDTO requestParam) {
        return Results.success(statsService.getShortlinkAccessRecord(requestParam));
    }

    /**
     * 访问分组指定时间内访问记录监控数据
     */
    @GetMapping("/api/shortlink/v1/stats/access-record/group")
    public Result<IPage<AccessRecordRespDTO>> groupStatsAccessRecord(GroupAccessRecordReqDTO requestParam) {
        return Results.success(statsService.getGroupAccessRecord(requestParam));
    }
}
