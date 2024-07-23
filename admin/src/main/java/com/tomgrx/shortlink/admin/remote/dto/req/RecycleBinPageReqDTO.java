package com.tomgrx.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 短链接回收站分页请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecycleBinPageReqDTO extends Page<RecycleBinPageReqDTO> {

    /**
     * 分组标识
     */
    private List<String> gidList;
}
