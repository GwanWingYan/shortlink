package com.tomgrx.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 短链接分组修改参数
 */
@Data
public class GroupUpdateReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组新名称
     */
    private String name;
}
