package com.tomgrx.shortlink.admin.remote.dto.resp;

import lombok.Data;

/**
 * 短链接分组查询返回参数
 */
@Data
public class GroupCountQueryRespDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接数量
     */
    private Integer count;
}
