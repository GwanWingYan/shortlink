package com.tomgrx.shortlink.constant;

/**
 * 短链接常量类
 */
public class ShortlinkConstant {

    /**
     * 永久短链接缓存有效时间（毫秒）
     */
    public static final long SHORTLINK_CACHE_VALID_DURATION = 2626560000L;

    /**
     * 空值跳转短链接缓存有效时间（分钟）
     */
    public static final long NULL_GOTO_CACHE_VALID_DURATION = 30L;

    /**
     * 单个用户的分组数量上限
     */
    public static final int DEFAULT_GROUP_MAX_NUM = 20;

    /**
     * 可重试操作的默认重试次数上限
     */
    public static final int DEFAULT_MAX_RETRY = 10;

    /**
     * 高德获取地区接口地址
     */
    public static final String AMAP_REMOTE_URL="https://restapi.amap.com/v3/ip";
}
