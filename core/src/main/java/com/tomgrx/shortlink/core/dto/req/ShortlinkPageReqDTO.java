package com.tomgrx.shortlink.core.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import lombok.Data;

/**
 * 短链接分页请求参数
 */
@Data
public class ShortlinkPageReqDTO extends Page<ShortlinkDO> {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序标识
     */
    private String orderTag;
}
