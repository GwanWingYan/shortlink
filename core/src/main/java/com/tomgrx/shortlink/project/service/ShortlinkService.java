package com.tomgrx.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tomgrx.shortlink.project.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.project.dto.biz.ShortlinkStatsRecordDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkUpdateReqDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkBatchCreateRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkCreateRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkGroupCountQueryRespDTO;
import com.tomgrx.shortlink.project.dto.resp.ShortlinkPageRespDTO;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 短链接接口层
 */
public interface ShortlinkService extends IService<ShortlinkDO> {

    /**
     * 创建短链接
     *
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建信息
     */
    ShortlinkCreateRespDTO createShortlink(ShortlinkCreateReqDTO requestParam);

    /**
     * 根据分布式锁创建短链接
     *
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建信息
     */
    ShortlinkCreateRespDTO createShortlinkByLock(ShortlinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
     *
     * @param requestParam 批量创建短链接请求参数
     * @return 批量创建短链接返回参数
     */
    ShortlinkBatchCreateRespDTO batchCreateShortlink(ShortlinkBatchCreateReqDTO requestParam);

    /**
     * 修改短链接
     *
     * @param requestParam 修改短链接请求参数
     */
    void updateShortlink(ShortlinkUpdateReqDTO requestParam);

    /**
     * 分页查询短链接
     *
     * @param requestParam 分页查询短链接请求参数
     * @return 短链接分页返回结果
     */
    IPage<ShortlinkPageRespDTO> pageShortlink(ShortlinkPageReqDTO requestParam);

    /**
     * 查询短链接分组内数量requestParam
     * @return
     */
    List<ShortlinkGroupCountQueryRespDTO> listGroupShortlinkCount(List<String> requestParam);


    /**
     * 短链接跳转
     *
     * @param shortUri 短链接后缀
     * @param request HTTP 请求
     * @param response HTTP 响应
     */
    void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) throws IOException;


    /**
     * 短链接统计
     *
     * @param shortLinkStatsRecord 短链接统计实体参数
     */
    void shortLinkStats(ShortlinkStatsRecordDTO shortLinkStatsRecord);
}
