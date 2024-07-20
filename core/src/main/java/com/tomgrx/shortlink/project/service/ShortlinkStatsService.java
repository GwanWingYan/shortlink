package com.tomgrx.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.project.dto.req.ShortlinkGroupStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkGroupStatsReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkStatsAccessRecordReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkStatsReqDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkStatsAccessRecordRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkStatsRespDTO;

public interface ShortlinkStatsService {

    /**
     * 获取单个短链接监控数据
     *
     * @param requestParam 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    ShortlinkStatsRespDTO oneShortlinkStats(ShortlinkStatsReqDTO requestParam);


    /**
     * 获取分组短链接监控数据
     *
     * @param requestParam 获取分组短链接监控数据入参
     * @return 分组短链接监控数据
     */
    ShortlinkStatsRespDTO groupShortlinkStats(ShortlinkGroupStatsReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     *
     * @param requestParam 获取短链接监控访问记录数据入参
     * @return 访问记录监控数据
     */
    IPage<ShortlinkStatsAccessRecordRespDTO> shortlinkStatsAccessRecord(ShortlinkStatsAccessRecordReqDTO requestParam);

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     *
     * @param requestParam 获取分组短链接监控访问记录数据入参
     * @return 分组访问记录监控数据
     */
    IPage<ShortlinkStatsAccessRecordRespDTO> groupShortlinkStatsAccessRecord(ShortlinkGroupStatsAccessRecordReqDTO requestParam);
}
