package com.tomgrx.shortlink.admin.remote.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 短链接基础访问监控返回参数
 */
@Data
public class ShortlinkStatsAccessDailyRespDTO {

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    /**
     * 访问量
     */
    private Integer pv;

    /**
     * 独立访客数
     */
    private Integer uv;

    /**
     * 独立IP数
     */
    private Integer uip;
}
