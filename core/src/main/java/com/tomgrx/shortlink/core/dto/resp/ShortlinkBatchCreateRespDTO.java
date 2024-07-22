package com.tomgrx.shortlink.core.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 短链接批量创建返回参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortlinkBatchCreateRespDTO {

    /**
     * 成功创建短链接的数量
     */
    private Integer total;

    /**
     * 批量创建短链接返回参数
     */
    private List<ShortlinkBaseInfoRespDTO> baseLinkInfos;
}
