package com.tomgrx.shortlink.core.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接访问设备监控返回参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatsRespDTO {

    /**
     * 统计
     */
    private Integer cnt;

    /**
     * 设备类型
     */
    private String device;

    /**
     * 占比
     */
    private Double ratio;
}
