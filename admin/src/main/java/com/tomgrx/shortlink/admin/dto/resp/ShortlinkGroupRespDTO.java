package com.tomgrx.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * 短链接分组返回实体
 */
@Data
public class ShortlinkGroupRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组用户名
     */
    private String userName;

    /**
     * 分组排序
     */
    private Integer sortOrder;

    /**
     * 分组短链接数量
     */
    private Integer shortLinkCount;
}
