package com.tomgrx.shortlink.admin.remote.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 短链接修改请求对象
 */
@Data
public class ShortlinkUpdateReqDTO {

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 短链接标识符
     */
    private String lid;

    /**
     * 原始分组标识
     */
    private String originGid;

    /**
     * 新分组标识
     */
    private String gid;

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
