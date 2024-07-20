package com.tomgrx.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tomgrx.shortlink.project.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.tomgrx.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkRecycleBinPageReqDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkPageRespDTO;

/**
 * 回收站管理接口层
 */
public interface RecycleBinService extends IService<ShortlinkDO> {

    /**
     * 保存回收站
     *
     * @param requestParam 请求参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查询回收站
     *
     * @param requestParam 分页查询回收站接请求参数
     * @return 分页查询回收站返回结果
     */
    IPage<ShortlinkPageRespDTO> pageShortlink(ShortlinkRecycleBinPageReqDTO requestParam);

    /**
     * 恢复短链接
     *
     * @param requestParam 请求参数
     */
    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);

    /**
     * 移除短链接
     *
     * @param requestParam 请求参数
     */
    void removeRecycleBin(RecycleBinRemoveReqDTO requestParam);
}
