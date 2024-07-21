package com.tomgrx.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.project.common.convention.result.Result;
import com.tomgrx.shortlink.project.common.convention.result.Results;
import com.tomgrx.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.tomgrx.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkRecycleBinPageReqDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站管理控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 将短链接移除到回收站
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站
     */
    @GetMapping("/api/shortlink/v1/recycle-bin/page")
    public Result<IPage<ShortlinkPageRespDTO>> pageShortlink(ShortlinkRecycleBinPageReqDTO requestParam) {
        return Results.success(recycleBinService.pageShortlink(requestParam));
    }

    /**
     * 从回收站还原短链接
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/recover")
    public Result<Void> recoverShortlink(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverShortlink(requestParam);
        return Results.success();
    }

    /**
     * 彻底移除短链接
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        recycleBinService.removeRecycleBin(requestParam);
        return Results.success();
    }
}
