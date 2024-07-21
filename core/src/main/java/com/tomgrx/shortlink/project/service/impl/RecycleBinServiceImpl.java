package com.tomgrx.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomgrx.shortlink.project.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.project.dao.mapper.ShortlinkMapper;
import com.tomgrx.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.tomgrx.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkRecycleBinPageReqDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.GOTO_IS_NULL_KEY_PREFIX;
import static com.tomgrx.shortlink.constant.RedisKeyConstant.GOTO_KEY_PREFIX;

@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortlinkMapper, ShortlinkDO> implements RecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                .eq(ShortlinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortlinkDO::getGid, requestParam.getGid())
                .eq(ShortlinkDO::getEnableStatus, 0)
                .eq(ShortlinkDO::getDelFlag, 0);
        ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                .enableStatus(1)
                .build();
        baseMapper.update(shortlinkDO, updateWrapper);
        stringRedisTemplate.delete(GOTO_KEY_PREFIX + requestParam.getFullShortUrl());
    }

    @Override
    public IPage<ShortlinkPageRespDTO> pageShortlink(ShortlinkRecycleBinPageReqDTO requestParam) {
        IPage<ShortlinkDO> resultPage = baseMapper.pageRecycleBinLink(requestParam);
        return resultPage.convert(each -> {
            ShortlinkPageRespDTO result = BeanUtil.toBean(each, ShortlinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
    }

    @Override
    public void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                .eq(ShortlinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortlinkDO::getGid, requestParam.getGid())
                .eq(ShortlinkDO::getEnableStatus, 1)
                .eq(ShortlinkDO::getDelFlag, 0);
        ShortlinkDO shortlinkDO = ShortlinkDO.builder()
                .enableStatus(0)
                .build();
        baseMapper.update(shortlinkDO, updateWrapper);
        stringRedisTemplate.delete(GOTO_IS_NULL_KEY_PREFIX + requestParam.getFullShortUrl());
    }

    @Override
    public void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortlinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortlinkDO.class)
                .eq(ShortlinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortlinkDO::getGid, requestParam.getGid())
                .eq(ShortlinkDO::getEnableStatus, 1)
                .eq(ShortlinkDO::getDelTime, 0L)
                .eq(ShortlinkDO::getDelFlag, 0);
        ShortlinkDO delShortlinkDO = ShortlinkDO.builder()
                .delTime(System.currentTimeMillis())
                .build();
        delShortlinkDO.setDelFlag(1);
        baseMapper.update(delShortlinkDO, updateWrapper);
    }
}
