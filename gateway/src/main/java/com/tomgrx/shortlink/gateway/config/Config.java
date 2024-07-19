package com.tomgrx.shortlink.gateway.config;

import lombok.Data;

import java.util.List;

/**
 * TokenValidate 过滤器的配置
 */
@Data
public class Config {

    /**
     * 请求路径与方法白名单
     */
    private List<Entry> whiteList;

    /**
     * Element 表示白名单中的一个请求路径与请求方法。
     * 以 /* 结尾的请求路径表示通配；不以 /* 结尾的请求路径表示精确匹配。
     * 请求方法要么是 *（表示通配），要么是 HTTP 请求方法（大小写无关）。
     */
    @Data
    public static class Entry {
        private String path;
        private String method;
    }
}
