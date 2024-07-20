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

import static com.tomgrx.shortlink.constant.RedisKeyConstant.LOCK_CREATE_USER_KEY;
import static com.tomgrx.shortlink.constant.RedisKeyConstant.LOGIN_KEY;
import static com.tomgrx.shortlink.admin.common.enums.UserErrorCodeEnum.*;

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

    @Override
    public UserRespDTO getUserByUsername(String userName) {
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

    @Override
    public Boolean hasUsername(String userName) {
        return !userNameBloomFilter.contains(userName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if (!hasUsername(requestParam.getUserName())) {
            throw new ClientException(USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(LOCK_CREATE_USER_KEY + requestParam.getUserName());
        if (!lock.tryLock()) {
            throw new ClientException(USER_NAME_EXIST);
        }
        try {
            int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
            if (inserted < 1) {
                throw new ClientException(USER_SAVE_ERROR);
            }
            groupService.createGroup(requestParam.getUserName(), "默认分组");
            userNameBloomFilter.add(requestParam.getUserName());
        } catch (DuplicateKeyException ex) {
            throw new ClientException(USER_EXIST);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        if (!Objects.equals(requestParam.getUserName(), UserContext.getUserName())) {
            throw new ClientException("当前登录用户修改请求异常");
        }
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUserName, requestParam.getUserName());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, requestParam.getUserName())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("用户不存在");
        }
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(LOGIN_KEY + requestParam.getUserName());
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            stringRedisTemplate.expire(LOGIN_KEY + requestParam.getUserName(), 30L, TimeUnit.MINUTES);
            String token = hasLoginMap.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户登录错误"));
            return new UserLoginRespDTO(token);
        }
        /**
         * Hash
         * Key：login_用户名
         * Value：
         *  Key：token标识
         *  Val：JSON 字符串（用户信息）
         */
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(LOGIN_KEY + requestParam.getUserName(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(LOGIN_KEY + requestParam.getUserName(), 30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String userName, String token) {
        return stringRedisTemplate.opsForHash().get(LOGIN_KEY + userName, token) != null;
    }

    @Override
    public void logout(String userName, String token) {
        if (checkLogin(userName, token)) {
            stringRedisTemplate.delete(LOGIN_KEY + userName);
            return;
        }
        throw new ClientException("用户Token不存在或用户未登录");
    }
}
