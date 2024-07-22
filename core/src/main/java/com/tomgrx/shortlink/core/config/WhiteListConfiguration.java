package com.tomgrx.shortlink.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 跳转域名白名单
 */
@Data
@Component
@ConfigurationProperties(prefix = "shortlink.white-list")
public class WhiteListConfiguration {

    /**
     * 是否开启跳转原始链接域名白名单验证
     */
    private Boolean enable;

    /**
     * 跳转原始域名白名单名称
     */
    private String name;

    /**
     * 可跳转的原始链接域名
     */
    private List<String> domains;
}
