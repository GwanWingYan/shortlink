package com.tomgrx.shortlink.core.dto.req;

import lombok.Data;

/**
 * 分组统计数据请求参数
 */
@Data
public class GroupStatsReqDTO {

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
}

