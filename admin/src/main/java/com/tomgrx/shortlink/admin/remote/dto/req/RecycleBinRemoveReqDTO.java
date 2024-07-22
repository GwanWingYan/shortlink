package com.tomgrx.shortlink.admin.remote.dto.req;

import lombok.Data;

/**
 * 回收站移除功能
 */
@Data
public class RecycleBinRemoveReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接标识符
     */
    private String lid;
}
