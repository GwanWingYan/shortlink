package com.tomgrx.shortlink.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.core.dto.biz.StatsRecordDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkUpdateReqDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkBatchCreateRespDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkCreateRespDTO;
import com.tomgrx.shortlink.core.dto.resp.GroupCountQueryRespDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkPageRespDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 短链接接口层
 */
public interface ShortlinkService extends IService<ShortlinkDO> {

    /**
     * 创建短链接
     */
    ShortlinkCreateRespDTO createShortlink(ShortlinkCreateReqDTO requestParam);

    /**
     * 创建短链接（使用分布式锁）
     */
    ShortlinkCreateRespDTO createShortlinkByLock(ShortlinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
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
     */
    IPage<ShortlinkPageRespDTO> pageShortlink(ShortlinkPageReqDTO requestParam);

    /**
     * 查询分组的短链接数量
     */
    List<GroupCountQueryRespDTO> listGroupShortlinkCount(List<String> gidList);


    /**
     * 短链接跳转
     *
     * @param code 短链接标识符
     * @param request HTTP 请求
     * @param response HTTP 响应
     */
    void gotoUrl(String code, HttpServletRequest request, HttpServletResponse response) throws IOException;


    /**
     * 短链接统计
     *
     * @param shortlinkStatsRecord 短链接统计实体参数
     */
    void sendStatsRecord(StatsRecordDTO shortlinkStatsRecord);
}
