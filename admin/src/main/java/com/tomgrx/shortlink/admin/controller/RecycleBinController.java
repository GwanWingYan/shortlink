package com.tomgrx.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.common.convention.result.Results;
import com.tomgrx.shortlink.admin.remote.CoreRemoteService;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinPageReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站控制层
 */
@RestController(value = "recycleBinControllerByAdmin")
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    private final CoreRemoteService coreRemoteService;

    /**
     * 将短链接移除到回收站
     */
    @PostMapping("/api/shortlink/admin/v1/recycle-bin/move-in")
    public Result<Void> moveIn(@RequestBody RecycleBinSaveReqDTO requestParam) {
        coreRemoteService.moveIn(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/shortlink/admin/v1/recycle-bin/page")
    public Result<Page<ShortlinkPageRespDTO>> pageQuery(RecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageQuery(requestParam);
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/shortlink/admin/v1/recycle-bin/move-out")
    public Result<Void> moveOut(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        coreRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 移除短链接
     */
    @PostMapping("/api/shortlink/admin/v1/recycle-bin/remove")
    public Result<Void> remove(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        coreRemoteService.removeRecycleBin(requestParam);
        return Results.success();
    }
}
