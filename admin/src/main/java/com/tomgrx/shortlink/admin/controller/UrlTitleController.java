package com.tomgrx.shortlink.admin.controller;

import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.remote.CoreRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * URL 标题控制层
 */
@RestController(value = "urlTitleControllerByAdmin")
@RequiredArgsConstructor
public class UrlTitleController {

    private final CoreRemoteService coreRemoteService;

    /**
     * 根据URL获取对应网站的标题
     */
    @GetMapping("/api/shortlink/admin/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url) {
        return coreRemoteService.getTitleByUrl(url);
    }
}
