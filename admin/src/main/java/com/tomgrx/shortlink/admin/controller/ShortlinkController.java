package com.tomgrx.shortlink.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.common.convention.result.Results;
import com.tomgrx.shortlink.admin.remote.CoreRemoteService;
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

import java.util.Date;
import java.util.List;

/**
 * 短链接管理控制层
 */
@RestController(value = "shortlinkControllerByAdmin")
@RequiredArgsConstructor
public class ShortlinkController {

    private final CoreRemoteService coreRemoteService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/shortlink/admin/v1/create")
    public Result<ShortlinkCreateRespDTO> createShortlink(@RequestBody ShortlinkCreateReqDTO requestParam) {
        return coreRemoteService.createShortlink(requestParam);
    }

    /**
     * 批量创建短链接，并返回 excel 文件展示批量创建短链接结果
     */
    @SneakyThrows
    @PostMapping("/api/shortlink/admin/v1/create/batch")
    public void batchCreateShortlink(@RequestBody ShortlinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortlinkBatchCreateRespDTO> shortlinkBatchCreateRespDTOResult = coreRemoteService.batchCreateShortlink(requestParam);
        if (shortlinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortlinkBaseInfoRespDTO> baseLinkInfos = shortlinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(
                    response,
                    "批量创建短链接结果-" + DateUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss"),
                    ShortlinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/shortlink/admin/v1/update")
    public Result<Void> updateShortlink(@RequestBody ShortlinkUpdateReqDTO requestParam) {
        coreRemoteService.updateShortlink(requestParam);
        return Results.success(null);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/shortlink/admin/v1/page")
    public Result<Page<ShortlinkPageRespDTO>> pageShortlink(@RequestBody ShortlinkPageReqDTO requestParam) {
        return coreRemoteService.pageShortlink(
                requestParam.getGid(),
                requestParam.getOrderTag(),
                requestParam.getCurrent(),
                requestParam.getSize()
        );
    }
}
