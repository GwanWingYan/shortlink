package com.tomgrx.shortlink.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tomgrx.shortlink.core.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 短链接实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_link")
public class ShortlinkDO extends BaseDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 短链接标识符
     */
    private String lid;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 点击量
     */
    private Integer click;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 启用标识: 0启用， 1未启用
     */
    private Integer enableFlag;

    /**
     * 创建类型: 0接口创建， 1控制台创建
     */
    private Integer createType;

    /**
     * 有效期类型: 0永久有效， 1自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    private Date validDate;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 网站标识
     */
    private String favicon;

    /**
     * 历史 PV
     */
    private Integer totalPv;

    /**
     * 历史 UV
     */
    private Integer totalUv;

    /**
     * 历史 UIP
     */
    private Integer totalUip;

    /**
     * 今日PV
     */
    @TableField(exist = false)
    private Integer todayPv;

    /**
     * 今日UV
     */
    @TableField(exist = false)
    private Integer todayUv;

    /**
     * 今日UIP
     */
    @TableField(exist = false)
    private Integer todayUip;


    /**
     * 删除时间
     */
    private Long deleteTime;
}
