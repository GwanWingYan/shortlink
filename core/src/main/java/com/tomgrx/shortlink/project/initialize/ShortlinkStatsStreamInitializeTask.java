package com.tomgrx.shortlink.project.initialize;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.STATS_STREAM_GROUP_KEY;
import static com.tomgrx.shortlink.constant.RedisKeyConstant.STATS_STREAM_TOPIC_KEY;

/**
 * 初始化短链接监控消息队列消费者组
 */
@Component
@RequiredArgsConstructor
public class ShortlinkStatsStreamInitializeTask implements InitializingBean {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        Boolean hasKey = stringRedisTemplate.hasKey(STATS_STREAM_TOPIC_KEY);
        if (hasKey == null || !hasKey) {
            stringRedisTemplate.opsForStream().createGroup(STATS_STREAM_TOPIC_KEY, STATS_STREAM_GROUP_KEY);
        }
    }
}
