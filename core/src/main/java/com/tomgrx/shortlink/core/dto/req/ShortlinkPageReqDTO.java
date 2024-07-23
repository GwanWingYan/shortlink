package com.tomgrx.shortlink.core.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 短链接分页请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ShortlinkPageReqDTO extends Page<ShortlinkPageReqDTO> {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序标识。
     * 可选值：todayPv，todayUv，todayUip，totalPv，totalUv，totalUip，或不选（默认按 create_time 排序）
     * 详情参考 core/src/main/resources/mapper/ShortlinkMapper.xml
     */
    private String orderTag;
}
