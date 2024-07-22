package com.tomgrx.shortlink.admin.dto.resp;

import lombok.Data;

import java.util.Date;

/**
 * 无脱敏用户信息返回实体
 */
@Data
public class UserActualRespDTO {
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
     * 手机号（无脱敏）
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deleteTime;

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
    private Integer deleteFlag;
}
