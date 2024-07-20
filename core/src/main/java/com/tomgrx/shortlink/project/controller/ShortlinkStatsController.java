package com.tomgrx.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.project.common.convention.result.Result;
import com.tomgrx.shortlink.project.common.convention.result.Results;
import com.tomgrx.shortlink.project.dto.req.ShortlinkGroupStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkGroupStatsReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkStatsReqDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkStatsAccessRecordRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkStatsRespDTO;
import com.tomgrx.shortlink.project.service.ShortlinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortlinkStatsController {

    private final ShortlinkStatsService shortlinkStatsService;

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/shortlink/v1/stats")
    public Result<ShortlinkStatsRespDTO> shortlinkStats(ShortlinkStatsReqDTO requestParam) {
        return Results.success(shortlinkStatsService.oneShortlinkStats(requestParam));
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/shortlink/v1/stats/group")
    public Result<ShortlinkStatsRespDTO> groupShortlinkStats(ShortlinkGroupStatsReqDTO requestParam) {
        return Results.success(shortlinkStatsService.groupShortlinkStats(requestParam));
    }

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/shortlink/v1/stats/access-record")
    public Result<IPage<ShortlinkStatsAccessRecordRespDTO>> shortlinkStatsAccessRecord(ShortlinkStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortlinkStatsService.shortlinkStatsAccessRecord(requestParam));
    }

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/shortlink/v1/stats/access-record/group")
    public Result<IPage<ShortlinkStatsAccessRecordRespDTO>> groupShortlinkStatsAccessRecord(ShortlinkGroupStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortlinkStatsService.groupShortlinkStatsAccessRecord(requestParam));
    }
}
