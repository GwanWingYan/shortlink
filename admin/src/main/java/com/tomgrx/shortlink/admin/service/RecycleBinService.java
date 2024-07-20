package com.tomgrx.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

/**
 * 回收站接口层
 */
public interface RecycleBinService {

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数，需包含 current (当前页数，从 1 开始) 和 size (每页大小)
     * @return 返回参数包装
     */
    Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
