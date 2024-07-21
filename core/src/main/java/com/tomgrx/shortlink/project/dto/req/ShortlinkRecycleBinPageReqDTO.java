package com.tomgrx.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.project.dao.entity.ShortlinkDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 回收站短链接分页请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShortlinkRecycleBinPageReqDTO extends Page<ShortlinkDO> {

    /**
     * 分组标识
     */
    private List<String> gidList;
}
