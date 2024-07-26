package com.tomgrx.shortlink.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinMoveOutReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinMoveInReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinPageQueryReqDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkPageRespDTO;

import java.util.List;

/**
 * 回收站管理接口层
 */
public interface RecycleBinService extends IService<ShortlinkDO> {

    /**
     * 将短链接移除到回收站
     */
    void moveIn(RecycleBinMoveInReqDTO requestParam);

    /**
     * 分页查询回收站中的短链接
     */
    IPage<ShortlinkPageRespDTO> pageQuery(RecycleBinPageQueryReqDTO requestParam);

    /**
     * 从回收站还原短链接
     */
    void moveOut(RecycleBinMoveOutReqDTO requestParam);

    /**
     * 彻底移除短链接
     */
    void remove(RecycleBinRemoveReqDTO requestParam);
}
