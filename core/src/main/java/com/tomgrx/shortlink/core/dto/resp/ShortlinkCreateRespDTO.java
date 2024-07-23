package com.tomgrx.shortlink.core.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接创建返回参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortlinkCreateRespDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 原始链接。必须以 http:// 或 https:// 开头。
     */
    private String originUrl;

    /**
     * 短链接标识符
     */
    private String lid;
}
