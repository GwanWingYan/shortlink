package com.tomgrx.shortlink.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.core.dao.mapper.ShortlinkMapper;
import com.tomgrx.shortlink.core.dto.req.RecycleBinMoveOutReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinMoveInReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinPageQueryReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.core.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.NULL_GOTO_KEY_PREFIX;
import static com.tomgrx.shortlink.constant.RedisKeyConstant.GOTO_KEY_PREFIX;

@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortlinkMapper, ShortlinkDO> implements RecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 将短链接移除到回收站
     */
    @Override
    public void moveIn(RecycleBinMoveInReqDTO requestParam) {
        LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                .eq(ShortlinkDO::getLid, requestParam.getLid())
                .eq(ShortlinkDO::getGid, requestParam.getGid())
                .eq(ShortlinkDO::getEnableFlag, 0)
                .eq(ShortlinkDO::getDeleteFlag, 0);
        ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                .enableFlag(1)
                .build();
        baseMapper.update(shortlinkDO, updateWrapper);
        stringRedisTemplate.delete(GOTO_KEY_PREFIX + requestParam.getLid());
    }

    /**
     * 分页查询回收站中的短链接
     */
    @Override
    public IPage<ShortlinkPageRespDTO> pageQuery(List<String> gidList, Integer current, Integer size) {
        RecycleBinPageQueryReqDTO requestParam = new RecycleBinPageQueryReqDTO(gidList);
        requestParam.setCurrent(current);
        requestParam.setSize(size);

        return baseMapper.pageRecycleBinLink(requestParam);
    }

    /**
     * 从回收站还原短链接
     */
    @Override
    public void moveOut(RecycleBinMoveOutReqDTO requestParam) {
        LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                .eq(ShortlinkDO::getLid, requestParam.getLid())
                .eq(ShortlinkDO::getGid, requestParam.getGid())
                .eq(ShortlinkDO::getEnableFlag, 1)
                .eq(ShortlinkDO::getDeleteFlag, 0);
        ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                .enableFlag(0)
                .build();
        baseMapper.update(shortlinkDO, updateWrapper);
        stringRedisTemplate.delete(NULL_GOTO_KEY_PREFIX + requestParam.getLid());
    }

    /**
     * 彻底移除短链接
     */
    @Override
    public void remove(RecycleBinRemoveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                .eq(ShortlinkDO::getLid, requestParam.getLid())
                .eq(ShortlinkDO::getGid, requestParam.getGid())
                .eq(ShortlinkDO::getEnableFlag, 1)
                .eq(ShortlinkDO::getDeleteTime, 0L)
                .eq(ShortlinkDO::getDeleteFlag, 0);
        ShortlinkDO delShortlinkDO = ShortlinkDO.builder()
                .deleteTime(System.currentTimeMillis())
                .build();
        delShortlinkDO.setDeleteFlag(1);
        baseMapper.update(delShortlinkDO, updateWrapper);
    }
}
