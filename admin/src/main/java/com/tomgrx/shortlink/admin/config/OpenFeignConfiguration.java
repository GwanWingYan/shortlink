package com.tomgrx.shortlink.admin.config;

import com.tomgrx.shortlink.admin.common.biz.user.UserContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign 微服务调用传递用户信息配置
 */
@Configuration
public class OpenFeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("userName", UserContext.getUserName());
            template.header("userId", UserContext.getUserId());
            template.header("realName", UserContext.getRealName());
        };
    }
}