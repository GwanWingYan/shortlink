package com.tomgrx.shortlink.core.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.core.dao.entity.AccessLogDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分组访问记录请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupAccessRecordReqDTO extends Page<AccessLogDO> {

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

