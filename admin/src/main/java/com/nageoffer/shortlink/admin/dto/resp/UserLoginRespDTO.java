package com.nageoffer.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户登录接口返回响应
 */
@Data
@AllArgsConstructor
public class UserLoginRespDTO {

    /**
     * 用户 Token
     */
    private String token;
}
