package com.tomgrx.shortlink.core.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel 限流配置
 */
@Component
public class SentinelRuleConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        List<FlowRule> rules = new ArrayList<>();
        // 创建一个流量控制规则
        FlowRule createOrderRule = new FlowRule();
        // 针对于名为 create_shortlink 的资源
        createOrderRule.setResource("create_shortlink");
        // 流量控制方式为 QPS
        createOrderRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // QPS 阈值为 1（即每秒最多处理 1 个请求）
        createOrderRule.setCount(1);
        rules.add(createOrderRule);
        FlowRuleManager.loadRules(rules);
    }
}
