package com.tomgrx.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tomgrx.shortlink.admin.common.serialize.PhoneDesensitizationSerializer;
import lombok.Data;

import java.util.Date;

/**
 * 用户返回参数响应
 */
@Data
public class UserRespDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;


    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deletionTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除标识: 0未删除， 1已删除
     */
    private Integer delFlag;
}
