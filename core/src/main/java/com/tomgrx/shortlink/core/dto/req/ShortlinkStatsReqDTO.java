package com.tomgrx.shortlink.core.dto.req;

import lombok.Data;

/**
 * 短链接监控请求参数
 */
@Data
public class ShortlinkStatsReqDTO {

    /**
     * 短链接标识符
     */
    private String lid;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 启用标识 0：启用 1：未启用
     */
    private Integer enableFlag;
}
