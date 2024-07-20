package com.tomgrx.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.common.convention.result.Results;
import com.tomgrx.shortlink.admin.remote.ShortlinkActualRemoteService;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkUpdateReqDTO;
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
public class ShortlinkController {

    private final ShortlinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/shortlink/admin/v1/create")
    public Result<ShortlinkCreateRespDTO> createShortlink(@RequestBody ShortlinkCreateReqDTO requestParam) {
        return shortLinkActualRemoteService.createShortlink(requestParam);
    }

    /**
     * 批量创建短链接，并返回 excel 文件展示批量创建短链接结果
     */
    @SneakyThrows
    @PostMapping("/api/shortlink/admin/v1/create/batch")
    public void batchCreateShortlink(@RequestBody ShortlinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortlinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortlink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortlinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接结果", ShortlinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/shortlink/admin/v1/update")
    public Result<Void> updateShortlink(@RequestBody ShortlinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortlink(requestParam);
        return Results.success(null);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/shortlink/admin/v1/page")
    public Result<Page<ShortlinkPageRespDTO>> pageShortlink(ShortlinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortlink(requestParam.getGid(), requestParam.getOrderTag(), requestParam.getCurrent(), requestParam.getSize());
    }
}
