package com.tomgrx.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tomgrx.shortlink.admin.dao.entity.UserDO;
import com.tomgrx.shortlink.admin.dto.req.UserLoginReqDTO;
import com.tomgrx.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.tomgrx.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.tomgrx.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.tomgrx.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUserName(String userName);

    /**
     * 查询用户名是否存在
     *
     * @param userName 用户名
     * @return 用户名存在返回 true，不存在返回 false
     */
    Boolean hasUserName(String userName);


    /**
     * 注册用户
     *
     * @param requestParam 注册用户请求参数
     */
    void createUser(UserRegisterReqDTO requestParam);

    /**
     * 根据用户名修改用户信息
     *
     * @param requestParam 修改用户请求参数
     */
    void updateUser(UserUpdateReqDTO requestParam);

    /**
     * 用户登录
     *
     * @param requestParam 用户登录请求参数
     * @return 用户登录返回参数 Token
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检查用户是否登录（即判断token是否有效）
     *
     * @param token 用户登录 Token
     * @return 如果token有效返回 true，如果token无效返回 false
     */
    Boolean isLogin(String userName, String token);

    /**
     * 用户退出登录
     */
    void logout();
}
