package com.tomgrx.shortlink.core.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接高频访问IP监控返回参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopIpStatsRespDTO {

    /**
     * 统计
     */
    private Integer cnt;

    /**
     * IP
     */
    private String ip;
}
