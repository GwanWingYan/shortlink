package com.tomgrx.shortlink.core.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.core.common.convention.result.Result;
import com.tomgrx.shortlink.core.common.convention.result.Results;
import com.tomgrx.shortlink.core.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkUpdateReqDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkBatchCreateRespDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkCreateRespDTO;
import com.tomgrx.shortlink.core.dto.resp.GroupCountQueryRespDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.core.handler.CustomBlockHandler;
import com.tomgrx.shortlink.core.service.ShortlinkService;
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

    private final ShortlinkService shortlinkService;

    /**
     * 短链接跳转原始链接
     */
    @GetMapping("/{lid}")
    public void gotoUrl(@PathVariable("lid") String lid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        shortlinkService.gotoUrl(lid, request, response);
    }

    /**
     * 创建短链接（使用布隆过滤器）。
     * 使用 Sentinel 进行流量控制；该 Sentinel 资源名称为 create_shortlink；指定 blockHandler
     */
    @PostMapping("/api/shortlink/v1/create")
    @SentinelResource(
            value = "create_shortlink",
            blockHandler = "createShortlinkBlockHandlerMethod",
            blockHandlerClass = CustomBlockHandler.class
    )
    public Result<ShortlinkCreateRespDTO> createShortlink(@RequestBody ShortlinkCreateReqDTO requestParam) {
        return Results.success(shortlinkService.createShortlink(requestParam));
    }

    /**
     * 创建短链接（使用分布式锁）
     */
    @PostMapping("/api/shortlink/v1/create/by-lock")
    public Result<ShortlinkCreateRespDTO> createShortlinkByLock(@RequestBody ShortlinkCreateReqDTO requestParam) {
        return Results.success(shortlinkService.createShortlinkByLock(requestParam));
    }

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/shortlink/v1/create/batch")
    public Result<ShortlinkBatchCreateRespDTO> batchCreateShortlink(@RequestBody ShortlinkBatchCreateReqDTO requestParam) {
        return Results.success(shortlinkService.batchCreateShortlink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/shortlink/v1/update")
    public Result<Void> updateShortlink(@RequestBody ShortlinkUpdateReqDTO requestParam) {
        shortlinkService.updateShortlink(requestParam);
        return Results.success(null);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/shortlink/v1/page")
    public Result<IPage<ShortlinkPageRespDTO>> pageShortlink(@RequestParam("gid") String gid,
                                                             @RequestParam("orderTag") String orderTag,
                                                             @RequestParam("current") Long current,
                                                             @RequestParam("size") Long size) {
        return Results.success(shortlinkService.pageShortlink(gid, orderTag, current, size));
    }

    /**
     * 查询分组的短链接数量
     */
    @GetMapping("/api/shortlink/v1/count")
    public Result<List<GroupCountQueryRespDTO>> listGroupShortlinkCount(@RequestParam("gidList") List<String> gidList) {
        return Results.success(shortlinkService.listGroupShortlinkCount(gidList));
    }
}
