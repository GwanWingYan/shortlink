package com.tomgrx.shortlink.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tomgrx.shortlink.core.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 访问日志实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_access_log")
public class AccessLogDO extends BaseDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 短链接标识符
     */
    private String lid;

    /**
     * 访客标识
     */
    private String vid;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * IP
     */
    private String ip;

    /**
     * 访问网络
     */
    private String network;

    /**
     * 访问设备
     */
    private String device;

    /**
     * 地区
     */
    private String locale;
}
