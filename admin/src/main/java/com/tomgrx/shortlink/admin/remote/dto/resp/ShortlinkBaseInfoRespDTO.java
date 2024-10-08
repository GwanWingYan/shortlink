package com.tomgrx.shortlink.admin.remote.dto.resp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接基础信息返回参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortlinkBaseInfoRespDTO {

    /**
     * 描述信息
     */
    @ExcelProperty("描述信息")
    @ColumnWidth(40)
    private String describe;

    /**
     * 短链接标识符
     */
    @ExcelProperty("短链接标识符")
    @ColumnWidth(40)
    private String lid;

    /**
     * 原始链接
     */
    @ExcelProperty("原始链接")
    @ColumnWidth(80)
    private String originUrl;
}
