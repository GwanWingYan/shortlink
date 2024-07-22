package com.tomgrx.shortlink.core.dto.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 短链接分页返回参数
 */
@Data
public class ShortlinkPageRespDTO {
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
     * 有效期类型: 0永久有效， 1自定义
     */
    private Integer validDateType;

    /**
     * 启用标识 0：启用 1：未启用
     */
    private Integer enableFlag;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
     * 今日 PV
     */
    private Integer todayPv;

    /**
     * 历史 UV
     */
    private Integer totalUv;

    /**
     * 今日 UV
     */
    private Integer todayUv;

    /**
     * 历史 UIP
     */
    private Integer totalUip;

    /**
     * 今日 UIP
     */
    private Integer todayUip;
}
