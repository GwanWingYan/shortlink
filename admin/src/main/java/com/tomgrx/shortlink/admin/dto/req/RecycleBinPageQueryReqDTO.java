package com.tomgrx.shortlink.admin.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 回收站短链接分页查询入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class RecycleBinPageQueryReqDTO extends Page<RecycleBinPageQueryReqDTO> {

    /**
     * 用户名称
     */
    private String userName;
}

