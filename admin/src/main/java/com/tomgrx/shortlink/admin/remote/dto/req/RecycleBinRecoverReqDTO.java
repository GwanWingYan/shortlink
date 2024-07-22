package com.tomgrx.shortlink.admin.remote.dto.req;

import lombok.Data;

/**
 * 回收站恢复功能
 */
@Data
public class RecycleBinRecoverReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接标识符
     */
    private String lid;
}
