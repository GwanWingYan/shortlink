package com.tomgrx.shortlink.admin.controller;

import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.common.convention.result.Results;
import com.tomgrx.shortlink.admin.dto.req.ShortlinkGroupSaveReqDTO;
import com.tomgrx.shortlink.admin.dto.req.ShortlinkGroupSortReqDTO;
import com.tomgrx.shortlink.admin.dto.req.ShortlinkGroupUpdateReqDTO;
import com.tomgrx.shortlink.admin.dto.resp.ShortlinkGroupRespDTO;
import com.tomgrx.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController(value = "groupControllerByAdmin")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 创建短链接分组
     */
    @PostMapping("/api/shortlink/admin/v1/group")
    public Result<Void> save(@RequestBody ShortlinkGroupSaveReqDTO requestParam) {
        groupService.createGroup(requestParam.getName());
        return Results.success();
    }


    /**
     * 查询短链接分组集合
     */
    @GetMapping("/api/shortlink/admin/v1/group")
    public Result<List<ShortlinkGroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }

    /**
     * 修改短链接分组名称
     */
    @PutMapping("/api/shortlink/admin/v1/group")
    public Result<Void> updateGroup(@RequestBody ShortlinkGroupUpdateReqDTO requestParam) {
        groupService.updateGroup(requestParam);
        return Results.success();
    }

    /**
     * 删除短链接分组
     */
    @DeleteMapping("/api/shortlink/admin/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid) {
        groupService.deleteGroup(gid);
        return Results.success();
    }

    /**
     * 排序短链接分组
     */
    @PostMapping("/api/shortlink/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<ShortlinkGroupSortReqDTO> requestParam) {
        groupService.sortGroup(requestParam);
        return Results.success();
    }
}
