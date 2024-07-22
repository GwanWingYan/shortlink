package com.tomgrx.shortlink.core.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 回收站短链接分页查询入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecycleBinPageQueryReqDTO extends Page<ShortlinkDO> {

    /**
     * 分组标识
     */
    private List<String> gidList;
}
