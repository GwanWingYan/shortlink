package com.tomgrx.shortlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 */
@Configuration(value = "rBloomFilterConfigurationByAdmin")
public class RBloomFilterConfiguration {

    /**
     * 用户名布隆过滤器，防止注册用户时查询数据库导致缓存穿透
     */
    @Bean
    public RBloomFilter<String> userNameBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userNameBloomFilter");
        cachePenetrationBloomFilter.tryInit(100_000_000L, 0.001);
        return cachePenetrationBloomFilter;
    }

    /**
     * 分组标识布隆过滤器，防止注册分组标识时查询数据库导致缓存穿透
     */
    @Bean
    public RBloomFilter<String> gidBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("gidBloomFilter");
        cachePenetrationBloomFilter.tryInit(200000000L, 0.001);
        return cachePenetrationBloomFilter;
    }
}
