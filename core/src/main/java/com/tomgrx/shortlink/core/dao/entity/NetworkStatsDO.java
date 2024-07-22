package com.tomgrx.shortlink.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tomgrx.shortlink.core.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 访问网络监控实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_network_stats")
public class NetworkStatsDO extends BaseDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 短链接标识符
     */
    private String lid;

    /**
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer cnt;

    /**
     * 访问网络
     */
    private String network;
}
