package com.tomgrx.shortlink.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.core.dto.biz.StatsRecordDTO;
import com.tomgrx.shortlink.core.dto.req.GroupAccessRecordReqDTO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkAccessRecordReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import com.tomgrx.shortlink.core.dto.resp.AccessRecordRespDTO;
import com.tomgrx.shortlink.core.dto.resp.StatsRespDTO;

public interface StatsService {

    /**
     * 获取单个短链接监控数据
     */
    StatsRespDTO getShortlinkStats(ShortlinkStatsReqDTO requestParam);

    /**
     * 获取分组短链接监控数据
     */
    StatsRespDTO getGroupStats(GroupStatsReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内访问记录
     */
    IPage<AccessRecordRespDTO> getShortlinkAccessRecord(ShortlinkAccessRecordReqDTO requestParam);

    /**
     * 访问分组指定时间内访问记录
     */
    IPage<AccessRecordRespDTO> getGroupAccessRecord(GroupAccessRecordReqDTO requestParam);

    /**
     * 保存监控记录
     */
    void saveStatsRecord(StatsRecordDTO statsRecord);
}
