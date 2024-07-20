package com.tomgrx.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.common.convention.result.Results;
import com.tomgrx.shortlink.admin.dto.req.UserLoginReqDTO;
import com.tomgrx.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.tomgrx.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.tomgrx.shortlink.admin.dto.resp.UserActualRespDTO;
import com.tomgrx.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.tomgrx.shortlink.admin.dto.resp.UserRespDTO;
import com.tomgrx.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController(value = "userControllerByAdmin")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/shortlink/admin/v1/user/{userName}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("userName") String userName) {
        return Results.success(userService.getUserByUsername(userName));
    }

    /**
     * 根据用户名查询无脱敏用户信息
     */
    @GetMapping("/api/shortlink/admin/v1/actual/user/{userName}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("userName") String userName) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(userName), UserActualRespDTO.class));
    }

    @GetMapping("/api/shortlink/admin/v1/user/has-userName")
    public Result<Boolean> hasUsername(@RequestParam("userName") String userName) {
        return Results.success(userService.hasUsername(userName));
    }

    /**
     * 注册用户
     */
    @PostMapping("/api/shortlink/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 更新用户
     */
    @PutMapping("/api/shortlink/admin/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam) {
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/shortlink/admin/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        return Results.success(userService.login(requestParam));
    }

    /**
     * 检查用户是否登录
     */
    @GetMapping("/api/shortlink/admin/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam("userName") String userName, @RequestParam("token") String token) {
        return Results.success(userService.checkLogin(userName, token));
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("/api/shortlink/admin/v1/user/logout")
    public Result<Void> logout(@RequestParam("userName") String userName, @RequestParam("token") String token) {
        userService.logout(userName, token);
        return Results.success();
    }
}
