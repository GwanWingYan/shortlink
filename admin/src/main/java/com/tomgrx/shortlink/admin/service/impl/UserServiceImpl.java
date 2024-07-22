package com.tomgrx.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomgrx.shortlink.admin.common.biz.user.UserContext;
import com.tomgrx.shortlink.admin.common.convention.exception.ClientException;
import com.tomgrx.shortlink.admin.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.tomgrx.shortlink.admin.dao.entity.UserDO;
import com.tomgrx.shortlink.admin.dao.mapper.UserMapper;
import com.tomgrx.shortlink.admin.dto.req.UserLoginReqDTO;
import com.tomgrx.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.tomgrx.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.tomgrx.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.tomgrx.shortlink.admin.dto.resp.UserRespDTO;
import com.tomgrx.shortlink.admin.service.GroupService;
import com.tomgrx.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.LOCK_CREATE_USER_KEY_PREFIX;
import static com.tomgrx.shortlink.constant.RedisKeyConstant.LOGIN_KEY_PREFIX;
import static com.tomgrx.shortlink.admin.common.enums.UserErrorCodeEnum.*;
import static com.tomgrx.shortlink.constant.UserConstant.DEFAULT_GROUP_NAME;
import static com.tomgrx.shortlink.constant.UserConstant.LOGIN_VALID_DURATION;

/**
 * 用户接口实现层
 */
@Service(value = "userServiceImplByAdmin")
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userNameBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final GroupService groupService;

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户返回实体
     */
    @Override
    public UserRespDTO getUserByUserName(String userName) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, userName);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ServiceException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    /**
     * 查询用户名是否存在
     *
     * @param userName 用户名
     * @return 用户名存在返回 true，不存在返回 false
     */
    @Override
    public Boolean hasUserName(String userName) {
        return userNameBloomFilter.contains(userName);
    }

    /**
     * 注册用户
     *
     * @param requestParam 注册用户请求参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createUser(UserRegisterReqDTO requestParam) {
        // 拒绝重复用户名
        if (!hasUserName(requestParam.getUserName())) {
            throw new ClientException(USER_NAME_EXIST);
        }
        // 加锁避免重复创建同一用户名
        RLock lock = redissonClient.getLock(LOCK_CREATE_USER_KEY_PREFIX + requestParam.getUserName());
        if (!lock.tryLock()) {
            throw new ClientException(USER_NAME_EXIST);
        }
        // 在数据库和缓存中创建用户和默认分组
        try {
            int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
            if (inserted < 1) {
                throw new ClientException(USER_SAVE_ERROR);
            }
            groupService.createGroup(requestParam.getUserName(), DEFAULT_GROUP_NAME);
            userNameBloomFilter.add(requestParam.getUserName());
        } catch (DuplicateKeyException ex) {
            throw new ClientException(USER_EXIST);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 根据用户名修改用户信息
     *
     * @param requestParam 修改用户请求参数
     */
    @Override
    public void updateUser(UserUpdateReqDTO requestParam) {
        if (!Objects.equals(requestParam.getUserName(), UserContext.getUserName())) {
            throw new ClientException("当前登录用户修改请求异常");
        }
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUserName, requestParam.getUserName());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    /**
     * 用户登录
     * 用一个 redis hash 存储一个用户的登录信息。Hash 结构如下：
     * Key: ${redisLoginKey}
     * Val:
     *      Key: ${本次登录的token}
     *      Val: ${用户信息JSON字符串}
     *
     * @param requestParam 用户登录请求参数
     * @return 用户登录返回参数 Token
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // 拒绝不存在的用户
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, requestParam.getUserName())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("用户不存在");
        }

        // 如果用户已登录，则刷新用户本次登录的到期时间
        String redisLoginKey = LOGIN_KEY_PREFIX + requestParam.getUserName();
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(redisLoginKey);
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            stringRedisTemplate.expire(redisLoginKey, 30L, TimeUnit.MINUTES);
            String token = hasLoginMap.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户登录错误"));
            return new UserLoginRespDTO(token);
        }

        // 如果用户未登录，则创建一个 redis hash 表示本次登录
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(redisLoginKey, token, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(redisLoginKey, LOGIN_VALID_DURATION, TimeUnit.MINUTES);
        return new UserLoginRespDTO(token);
    }

    /**
     * 检查用户是否登录（即判断token是否有效）
     *
     * @param token 用户登录 Token
     * @return 如果token有效返回 true，如果token无效返回 false
     */
    @Override
    public Boolean isLogin(String userName, String token) {
        return stringRedisTemplate.opsForHash().get(LOGIN_KEY_PREFIX + userName, token) != null;
    }

    /**
     * 用户退出登录
     */
    @Override
    public void logout(String userName, String token) {
        if (isLogin(userName, token)) {
            stringRedisTemplate.delete(LOGIN_KEY_PREFIX + userName);
            return;
        }
        throw new ClientException("用户Token不存在或用户未登录");
    }
}
