package com.tomgrx.shortlink.core.dto.req;

import lombok.Data;

/**
 * 回收站恢复短链接请求参数
 */
@Data
public class RecycleBinMoveOutReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接标识符
     */
    private String lid;
}
