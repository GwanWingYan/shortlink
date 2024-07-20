package com.tomgrx.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.common.convention.result.Results;
import com.tomgrx.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.*;
import com.tomgrx.shortlink.admin.toolkit.EasyExcelWebUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接管理控制层
 */
@RestController(value = "shortLinkControllerByAdmin")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/shortlink/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkActualRemoteService.createShortLink(requestParam);
    }

    /**
     * 批量创建短链接，并返回 excel 文件展示批量创建短链接结果
     */
    @SneakyThrows
    @PostMapping("/api/shortlink/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接结果", ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/shortlink/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortLink(requestParam);
        return Results.success(null);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/shortlink/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam.getGid(), requestParam.getOrderTag(), requestParam.getCurrent(), requestParam.getSize());
    }
}
