package com.tomgrx.shortlink.core.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 短链接创建请求对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortlinkCreateReqDTO {

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 创建类型: 0接口创建， 1控制台创建
     */
    private Integer createType;

    /**
     * 有效期类型: 0永久有效， 1自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    /**
     * 描述
     */
    private String describe;
}
