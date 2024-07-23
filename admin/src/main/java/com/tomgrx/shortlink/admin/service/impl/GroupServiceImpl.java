package com.tomgrx.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomgrx.shortlink.admin.common.biz.user.UserContext;
import com.tomgrx.shortlink.admin.common.convention.exception.ClientException;
import com.tomgrx.shortlink.admin.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.dao.entity.GroupDO;
import com.tomgrx.shortlink.admin.dao.entity.GroupUniqueDO;
import com.tomgrx.shortlink.admin.dao.mapper.GroupMapper;
import com.tomgrx.shortlink.admin.dao.mapper.GroupUniqueMapper;
import com.tomgrx.shortlink.admin.dto.req.GroupSortReqDTO;
import com.tomgrx.shortlink.admin.dto.req.GroupUpdateReqDTO;
import com.tomgrx.shortlink.admin.dto.resp.GroupRespDTO;
import com.tomgrx.shortlink.admin.remote.CoreRemoteService;
import com.tomgrx.shortlink.admin.remote.dto.resp.GroupCountQueryRespDTO;
import com.tomgrx.shortlink.admin.service.GroupService;
import com.tomgrx.shortlink.admin.toolkit.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.LOCK_CREATE_GROUP_KEY_PREFIX;
import static com.tomgrx.shortlink.constant.ShortlinkConstant.DEFAULT_GROUP_MAX_NUM;
import static com.tomgrx.shortlink.constant.ShortlinkConstant.DEFAULT_MAX_RETRY;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service(value = "groupServiceImplByAdmin")
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    private final RBloomFilter<String> gidBloomFilter;
    private final GroupUniqueMapper groupUniqueMapper;
    private final CoreRemoteService coreRemoteService;
    private final RedissonClient redissonClient;

    @Value("${shortlink.group.max-num}")
    private Integer groupMaxNum = DEFAULT_GROUP_MAX_NUM;

    @Value("${shortlink.max-retry}")
    private Integer maxRetry = DEFAULT_MAX_RETRY;

    /**
     * 创建短链接分组
     *
     * @param groupName 短链接分组名称
     */
    @Override
    public void createGroup(String groupName) {
        createGroup(UserContext.getUserName(), groupName);
    }

    /**
     * 创建短链接分组
     *
     * @param userName 用户名
     * @param groupName 短链接分组名称
     */
    @Override
    public void createGroup(String userName, String groupName) {
        // 使用 Redis 分布式锁保证同一分组不被重复创建
        RLock lock = redissonClient.getLock(LOCK_CREATE_GROUP_KEY_PREFIX + userName);
        lock.lock();
        try {
            // 拒绝分组数已达上限的用户创建分组
            LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                    .eq(GroupDO::getUserName, userName)
                    .eq(GroupDO::getDeleteFlag, 0);
            List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
            if (CollUtil.isNotEmpty(groupDOList) && groupDOList.size() == groupMaxNum) {
                throw new ClientException(String.format("当前用户分组数已达上限：%d", groupMaxNum));
            }

            // 创建分组
            String gid = createUniqueGroupId();
            GroupDO groupDO = GroupDO.builder()
                    .gid(gid)
                    .sortOrder(0)
                    .userName(userName)
                    .name(groupName)
                    .build();
            baseMapper.insert(groupDO);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 查询用户短链接分组集合
     *
     * @return 用户短链接分组集合
     */
    @Override
    public List<GroupRespDTO> listGroup() {
        // 查询用户的所有分组信息，不含分组的短链接数量
        @SuppressWarnings("unchecked")
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDeleteFlag, 0)
                .eq(GroupDO::getUserName, UserContext.getUserName())
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);

        // 查询各个分组的短链接数量
        Result<List<GroupCountQueryRespDTO>> groupShortlinkCountList = coreRemoteService
                .listGroupShortlinkCount(groupDOList.stream().map(GroupDO::getGid).toList());

        // 将各分组短链接数量加入到返回结果中
        List<GroupRespDTO> groupRespDTOList = BeanUtil.copyToList(groupDOList, GroupRespDTO.class);
        groupRespDTOList.forEach(each -> {
            Optional<GroupCountQueryRespDTO> first = groupShortlinkCountList.getData().stream()
                    .filter(item -> Objects.equals(item.getGid(), each.getGid()))
                    .findFirst();
            first.ifPresent(item -> each.setShortlinkCount(first.get().getCount()));
            first.ifPresentOrElse(
                    item -> each.setShortlinkCount(item.getCount()),
                    () -> each.setShortlinkCount(0));
        });
        return groupRespDTOList;
    }

    /**
     * 修改短链接分组
     *
     * @param requestParam 修改短链接分组参数
     */
    @Override
    public void updateGroup(GroupUpdateReqDTO requestParam) {
        GroupDO groupDO = GroupDO.builder()
                .name(requestParam.getName())
                .build();
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUserName, UserContext.getUserName())
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDeleteFlag, 0);
        baseMapper.update(groupDO, updateWrapper);
    }

    /**
     * 删除短链接分组
     *
     * @param gid 短链接分组标识
     */
    @Override
    public void deleteGroup(String gid) {
        GroupDO groupDO = new GroupDO();
        groupDO.setDeleteFlag(1);
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUserName, UserContext.getUserName())
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDeleteFlag, 0);
        baseMapper.update(groupDO, updateWrapper);
    }

    /**
     * 短链接分组排序
     *
     * @param requestParam 短链接分组排序参数
     */
    @Override
    public void sortGroup(List<GroupSortReqDTO> requestParam) {
        requestParam.forEach(each -> {
            GroupDO groupDO = GroupDO.builder()
                    .sortOrder(each.getSortOrder())
                    .build();
            LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getUserName, UserContext.getUserName())
                    .eq(GroupDO::getGid, each.getGid())
                    .eq(GroupDO::getDeleteFlag, 0);
            baseMapper.update(groupDO, updateWrapper);
        });
    }

    /**
     * 创建一个唯一分组标识
     *
     * @return 唯一分组标识
     * @throws ServiceException 如果经历多次重试仍无法创建唯一分组标识，则抛出 ServiceException
     */
    private String createUniqueGroupId() {
        for (int retry = 0; retry < maxRetry; retry++) {
            String gid = RandomGenerator.generateRandom();

            // 拒绝已存在于布隆过滤器中的分组标识
            if (gidBloomFilter.contains(gid)) {
                continue;
            }

            // 拒绝已存在于数据库中的分组标识
            GroupUniqueDO groupUniqueDO = GroupUniqueDO.builder()
                    .gid(gid)
                    .build();
            try {
                groupUniqueMapper.insert(groupUniqueDO);
            } catch (DuplicateKeyException e) {
                continue;
            }

            // 返回全新的唯一分组标识
            gidBloomFilter.add(gid);
            return gid;
        }

        throw new ServiceException("生成分组标识过于频繁");
    }
}
