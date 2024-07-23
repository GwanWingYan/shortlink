package com.tomgrx.shortlink.core.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 回收站短链接分页查询入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class RecycleBinPageQueryReqDTO extends Page<RecycleBinPageQueryReqDTO> {

    /**
     * 分组标识
     */
    private List<String> gidList;
}
