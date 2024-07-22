package com.tomgrx.shortlink.core.mq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.STATS_STREAM_TOPIC_KEY;

/**
 * 基于 Redis Stream 的的统计数据发送者
 */
@Component
@RequiredArgsConstructor
public class RedisStreamStatsProducer {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 发送统计数据
     */
    public void send(Map<String, String> producerMap) {
        stringRedisTemplate.opsForStream().add(STATS_STREAM_TOPIC_KEY, producerMap);
    }
}
