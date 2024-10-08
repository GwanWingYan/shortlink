package com.tomgrx.shortlink.constant;

/**
 * Redis 缓存常量
 */
public class RedisKeyConstant {

    /**
     * 用户注册锁
     */
    public static final String LOCK_CREATE_USER_KEY_PREFIX = "shortlink:lock:create-user:";

    /**
     * 分组创建锁
     */
    public static final String LOCK_CREATE_GROUP_KEY_PREFIX = "shortlink:lock:create-group:";

    /**
     * 短链接创建锁
     */
    public static final String LOCK_CREATE_SHORTLINK_KEY = "shortlink:lock:create-shortlink";

    /**
     * 用户登录缓存标识
     */
    public static final String LOGIN_KEY_PREFIX = "shortlink:login:";

    /**
     * 短链接跳转
     */
    public static final String GOTO_KEY_PREFIX = "shortlink:goto:";

    /**
     * 短链接空值跳转
     */
    public static final String NULL_GOTO_KEY_PREFIX = "shortlink:null-goto:";

    /**
     * 短链接跳转锁
     */
    public static final String LOCK_GOTO_KEY_PREFIX = "shortlink:lock:goto:";

    /**
     * 修改分组标识锁
     */
    public static final String LOCK_GID_UPDATE_KEY_PREFIX = "shortlink:lock:update-gid:";

    /**
     * 保存监控记录锁
     */
    public static final String LOCK_SAVE_STATS_KEY_PREFIX = "shortlink:lock:save-stats:";

    /**
     * 访客数量统计
     */
    public static final String UV_STATS_KEY_PREFIX = "shortlink:stats:uv:";

    /**
     * IP数量统计
     */
    public static final String UIP_STATS_KEY_PREFIX = "shortlink:stats:uip:";

    /**
     * 延迟阻塞队列
     */
    public static final String STATS_DELAY_QUEUE_KEY = "shortlink:stats:delay-queue";

    /**
     * 监控消息保存队列 Topic 缓存标识
     * TODO: 优化注释
     */
    public static final String STATS_STREAM_TOPIC_KEY = "shortlink:stats-stream";

    /**
     * 监控消息保存队列 Group 缓存标识
     * TODO: 优化注释
     */
    public static final String STATS_STREAM_GROUP_KEY = "shortlink:stats-stream:only-group";
}
