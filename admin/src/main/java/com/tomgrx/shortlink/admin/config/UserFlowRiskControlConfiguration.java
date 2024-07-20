package com.tomgrx.shortlink.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用户操作流量风控配置文件
 */
@Data
@Component
@ConfigurationProperties(prefix = "shortlink.flow-limit")
public class UserFlowRiskControlConfiguration {

    /**
     * 是否开启用户流量风控验证
     */
    private Boolean enable = true;

    /**
     * 流量风控时间窗口，单位：秒
     */
    private String timeWindow = "60";

    /**
     * 流量风控时间窗口内可访问次数
     */
    private Long maxAccessCount = 100L;
}
