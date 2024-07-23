package com.tomgrx.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 用户信息更新请求参数
 * 注：只要传入的字段不是 null，哪怕为空字符串，mybatis plus 都会修改
 */
@Data
public class UserUpdateReqDTO {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}
