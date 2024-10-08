package com.tomgrx.shortlink.constant;

public class UserConstant {

    /**
     * 用户单次登录有效期（分钟）
     */
    public static final long LOGIN_VALID_DURATION = 24 * 60L;

    /**
     * 用户Cookie标识有效期（秒）
     */
    public static final int COOKIE_VALID_DURATION = 60 * 60 * 24 * 30;

    /**
     * 用户默认短链接分组名称
     */
    public static final String DEFAULT_GROUP_NAME = "默认分组";
}
