package com.tomgrx.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.biz.user.UserContext;
import com.tomgrx.shortlink.admin.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.dao.entity.GroupDO;
import com.tomgrx.shortlink.admin.dao.mapper.GroupMapper;
import com.tomgrx.shortlink.admin.remote.CoreRemoteService;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinPageReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * URL 回收站接口实现层
 */
@Service(value = "recycleBinServiceImplByAdmin")
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final CoreRemoteService coreRemoteService;
    private final GroupMapper groupMapper;

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数，需包含 current (当前页数，从 1 开始) 和 size (每页大小)
     * @return 返回参数包装
     */
    @Override
    public Result<Page<ShortlinkPageRespDTO>> pageQuery(RecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUserName, UserContext.getUserName())
                .eq(GroupDO::getDeleteFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("用户无分组信息");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return coreRemoteService.pageRecycleBinShortlink(
                requestParam.getGidList(),
                requestParam.getCurrent(),
                requestParam.getSize()
        );
    }
}
