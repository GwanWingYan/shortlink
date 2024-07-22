package com.tomgrx.shortlink.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tomgrx.shortlink.core.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 浏览器统计访问实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_browser_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrowserStatsDO extends BaseDO {

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
     * 浏览器
     */
    private String browser;
}
