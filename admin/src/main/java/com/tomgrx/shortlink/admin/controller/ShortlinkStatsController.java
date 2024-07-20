package com.tomgrx.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.remote.ShortlinkActualRemoteService;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkGroupStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkGroupStatsReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkStatsReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkStatsAccessRecordRespDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkStatsRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController(value = "shortLinkStatsControllerByAdmin")
@RequiredArgsConstructor
public class ShortlinkStatsController {

    private final ShortlinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/shortlink/admin/v1/stats")
    public Result<ShortlinkStatsRespDTO> shortLinkStats(ShortlinkStatsReqDTO requestParam) {
        return shortLinkActualRemoteService.oneShortlinkStats(
                requestParam.getFullShortUrl(),
                requestParam.getGid(),
                requestParam.getEnableStatus(),
                requestParam.getStartDate(),
                requestParam.getEndDate()
        );
    }

    /**
     * 访问分组指定时间内监控数据
     */
    @GetMapping("/api/shortlink/admin/v1/stats/group")
    public Result<ShortlinkStatsRespDTO> groupShortlinkStats(ShortlinkGroupStatsReqDTO requestParam) {
        return shortLinkActualRemoteService.groupShortlinkStats(
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate()
        );
    }

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/shortlink/admin/v1/stats/access-record")
    public Result<Page<ShortlinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortlinkStatsAccessRecordReqDTO requestParam) {
        return shortLinkActualRemoteService.shortLinkStatsAccessRecord(
                requestParam.getFullShortUrl(),
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                requestParam.getEnableStatus(),
                requestParam.getCurrent(),
                requestParam.getSize()
        );
    }

    /**
     * 访问分组指定时间内访问记录监控数据
     */
    @GetMapping("/api/shortlink/admin/v1/stats/access-record/group")
    public Result<Page<ShortlinkStatsAccessRecordRespDTO>> groupShortlinkStatsAccessRecord(ShortlinkGroupStatsAccessRecordReqDTO requestParam) {
        return shortLinkActualRemoteService.groupShortlinkStatsAccessRecord(
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                requestParam.getCurrent(),
                requestParam.getSize()
        );
    }
}
