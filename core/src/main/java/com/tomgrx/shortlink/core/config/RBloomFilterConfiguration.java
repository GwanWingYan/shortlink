package com.tomgrx.shortlink.core.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 */
@Configuration
public class RBloomFilterConfiguration {

    /**
     * 短链接标识符布隆过滤器，防止创建短链接时查询数据库导致缓存穿透
     */
    @Bean
    public RBloomFilter<String> lidBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("lidBloomFilter");
        cachePenetrationBloomFilter.tryInit(100_000_000L, 0.001);
        return cachePenetrationBloomFilter;
    }
}
