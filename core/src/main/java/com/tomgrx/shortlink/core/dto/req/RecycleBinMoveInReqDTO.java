package com.tomgrx.shortlink.core.dto.req;

import lombok.Data;

/**
 * 将短链接移除到回收站请求参数
 */
@Data
public class RecycleBinMoveInReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接标识符
     */
    private String lid;
}
