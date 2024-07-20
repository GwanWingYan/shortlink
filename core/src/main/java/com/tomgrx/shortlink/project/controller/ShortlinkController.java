package com.tomgrx.shortlink.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.project.common.convention.result.Result;
import com.tomgrx.shortlink.project.common.convention.result.Results;
import com.tomgrx.shortlink.project.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkUpdateReqDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkBatchCreateRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkCreateRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkGroupCountQueryRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.project.handler.CustomBlockHandler;
import com.tomgrx.shortlink.project.service.ShortlinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 短链接控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortlinkController {

    private final ShortlinkService shortLinkService;

    /**
     * 短链接跳转原始链接
     */
    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, HttpServletRequest request, HttpServletResponse response) throws IOException {
        shortLinkService.restoreUrl(shortUri, request, response);
    }

    /**
     * 创建短链接
     */
    @PostMapping("/api/shortlink/v1/create")
    @SentinelResource(
            value = "create_shortlink",
            blockHandler = "createShortlinkBlockHandlerMethod",
            blockHandlerClass = CustomBlockHandler.class
    )
    public Result<ShortlinkCreateRespDTO> createShortlink(@RequestBody ShortlinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortlink(requestParam));
    }

    /**
     * 通过分布式锁创建短链接
     */
    @PostMapping("/api/shortlink/v1/create/by-lock")
    public Result<ShortlinkCreateRespDTO> createShortlinkByLock(@RequestBody ShortlinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortlinkByLock(requestParam));
    }

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/shortlink/v1/create/batch")
    public Result<ShortlinkBatchCreateRespDTO> batchCreateShortlink(@RequestBody ShortlinkBatchCreateReqDTO requestParam) {
        return Results.success(shortLinkService.batchCreateShortlink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/shortlink/v1/update")
    public Result<Void> updateShortlink(@RequestBody ShortlinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortlink(requestParam);
        return Results.success(null);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/shortlink/v1/page")
    public Result<IPage<ShortlinkPageRespDTO>> pageShortlink(ShortlinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortlink(requestParam));
    }

    /**
     * 查询短链接分组内数量
     * @return
     */
    @GetMapping("/api/shortlink/v1/count")
    public Result<List<ShortlinkGroupCountQueryRespDTO>> listGroupShortlinkCount(@RequestParam("requestParam") List<String> requestParam) {
        return Results.success(shortLinkService.listGroupShortlinkCount(requestParam));
    }
}
