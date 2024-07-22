package com.tomgrx.shortlink.core.dto.biz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 短链接统计实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsRecordDTO {

    /**
     * 短链接标识符
     */
    private String lid;

    /**
     * 访客标识
     */
    private String vid;

    /**
     * 访客IP
     */
    private String vip;

    /**
     * 是否为该访客的初次访问
     */
    private Boolean isVisitorFirstVisit;

    /**
     * 是否为该IP的初次访问
     */
    private Boolean isIpFirstVisit;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作设备
     */
    private String device;

    /**
     * 网络
     */
    private String network;

    /**
     * 消息队列唯一标识
     */
    private String keys;

    /**
     * 当前时间
     */
    private Date currentDate;
}
