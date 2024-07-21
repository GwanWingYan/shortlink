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
     * 根据用户名查询用户信息（手机号已脱敏）
     */
    @GetMapping("/api/shortlink/admin/v1/user/{user-name}")
    public Result<UserRespDTO> getUserByUserName(@PathVariable("user-name") String userName) {
        return Results.success(userService.getUserByUserName(userName));
    }

    /**
     * 根据用户名查询用户信息（手机号未脱敏）
     */
    @GetMapping("/api/shortlink/admin/v1/user/actual/{user-name}")
    public Result<UserActualRespDTO> getActualUserByUserName(@PathVariable("user-name") String userName) {
        return Results.success(BeanUtil.toBean(userService.getUserByUserName(userName), UserActualRespDTO.class));
    }

    /**
     * 查询用户名是否存在
     */
    @GetMapping("/api/shortlink/admin/v1/user/has-user-name")
    public Result<Boolean> hasUserName(@RequestParam("user-name") String userName) {
        return Results.success(userService.hasUserName(userName));
    }

    /**
     * 注册用户
     */
    @PostMapping("/api/shortlink/admin/v1/user")
    public Result<Void> createUser(@RequestBody UserRegisterReqDTO requestParam) {
        userService.createUser(requestParam);
        return Results.success();
    }

    /**
     * 更新用户
     */
    @PutMapping("/api/shortlink/admin/v1/user")
    public Result<Void> updateUser(@RequestBody UserUpdateReqDTO requestParam) {
        userService.updateUser(requestParam);
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
     * 检查用户是否登录（即判断token是否有效）
     */
    @GetMapping("/api/shortlink/admin/v1/user/is-login")
    public Result<Boolean> isLogin(@RequestParam("user-name") String userName, @RequestParam("token") String token) {
        return Results.success(userService.isLogin(userName, token));
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("/api/shortlink/admin/v1/user/logout")
    public Result<Void> logout(@RequestParam("user-name") String userName, @RequestParam("token") String token) {
        userService.logout(userName, token);
        return Results.success();
    }
}
