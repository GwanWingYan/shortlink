package com.tomgrx.shortlink.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.core.common.convention.result.Result;
import com.tomgrx.shortlink.core.common.convention.result.Results;
import com.tomgrx.shortlink.core.dto.req.RecycleBinMoveOutReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinMoveInReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinPageQueryReqDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.core.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @PostMapping("/api/shortlink/v1/recycle-bin/move-in")
    public Result<Void> moveIn(@RequestBody RecycleBinMoveInReqDTO requestParam) {
        recycleBinService.moveIn(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站中的短链接
     */
    @GetMapping("/api/shortlink/v1/recycle-bin/page")
    public Result<IPage<ShortlinkPageRespDTO>> pageQuery(RecycleBinPageQueryReqDTO requestParam) {
        return Results.success(recycleBinService.pageQuery(requestParam));
    }

    /**
     * 从回收站还原短链接
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/move-out")
    public Result<Void> moveOut(@RequestBody RecycleBinMoveOutReqDTO requestParam) {
        recycleBinService.moveOut(requestParam);
        return Results.success();
    }

    /**
     * 彻底移除短链接
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/remove")
    public Result<Void> remove(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        recycleBinService.remove(requestParam);
        return Results.success();
    }
}
