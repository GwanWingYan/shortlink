package com.tomgrx.shortlink.core.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接基础信息返回参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortlinkBaseInfoRespDTO {

    /**
     * 描述信息
     */
    private String describe;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 短链接标识符
     */
    private String lid;
}
