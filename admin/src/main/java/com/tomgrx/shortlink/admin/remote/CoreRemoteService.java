package com.tomgrx.shortlink.admin.remote;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tomgrx.shortlink.admin.common.convention.result.Result;
import com.tomgrx.shortlink.admin.config.OpenFeignConfiguration;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkBatchCreateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkCreateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.req.ShortlinkUpdateReqDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkBatchCreateRespDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkCreateRespDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.GroupCountQueryRespDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkPageRespDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkStatsAccessRecordRespDTO;
import com.tomgrx.shortlink.admin.remote.dto.resp.ShortlinkStatsRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 短链接核心服务远程调用
 */
@FeignClient(
        value = "shortlink-core",
        configuration = OpenFeignConfiguration.class
)
public interface CoreRemoteService {

    /**
     * 创建短链接
     *
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建响应
     */
    @PostMapping("/api/shortlink/v1/create")
    Result<ShortlinkCreateRespDTO> createShortlink(@RequestBody ShortlinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
     *
     * @param requestParam 批量创建短链接请求参数
     * @return 短链接批量创建响应
     */
    @PostMapping("/api/shortlink/v1/create/batch")
    Result<ShortlinkBatchCreateRespDTO> batchCreateShortlink(@RequestBody ShortlinkBatchCreateReqDTO requestParam);

    /**
     * 修改短链接
     *
     * @param requestParam 修改短链接请求参数
     */
    @PostMapping("/api/shortlink/v1/update")
    void updateShortlink(@RequestBody ShortlinkUpdateReqDTO requestParam);

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/shortlink/v1/page")
    Result<Page<ShortlinkPageRespDTO>> pageShortlink(@RequestParam("gid") String gid,
                                                     @RequestParam("orderTag") String orderTag,
                                                     @RequestParam("current") Long current,
                                                     @RequestParam("size") Long size);

    /**
     * 查询分组短链接总量
     */
    @GetMapping("/api/shortlink/v1/count")
    Result<List<GroupCountQueryRespDTO>> listGroupShortlinkCount(@RequestParam("gidList") List<String> gidList);

    /**
     * 根据 URL 获取标题
     *
     * @param url 目标网站地址
     * @return 网站标题
     */
    @GetMapping("/api/shortlink/v1/title")
    Result<String> getTitleByUrl(@RequestParam("url") String url);

    /**
     * 保存回收站
     *
     * @param requestParam 请求参数
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/move-in")
    void moveIn(@RequestBody RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查询回收站短链接
     *
     * @param current 当前页
     * @param size    当前数据多少
     * @return 查询短链接响应
     */
    @GetMapping("/api/shortlink/v1/recycle-bin/page")
    Result<Page<ShortlinkPageRespDTO>> pageRecycleBinShortlink(@RequestParam("current") Long current,
                                                               @RequestParam("size") Long size);

    /**
     * 恢复短链接
     *
     * @param requestParam 短链接恢复请求参数
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/move-out")
    void recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam);

    /**
     * 移除短链接
     *
     * @param requestParam 短链接移除请求参数
     */
    @PostMapping("/api/shortlink/v1/recycle-bin/remove")
    void removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam);


    /**
     * 访问单个短链接指定时间内监控数据
     *
     * @param lid       短链接标识符
     * @param gid       分组标识
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 短链接监控信息
     */
    @GetMapping("/api/shortlink/v1/stats")
    Result<ShortlinkStatsRespDTO> shortlinkStats(@RequestParam("lid") String lid,
                                                 @RequestParam("gid") String gid,
                                                 @RequestParam("enableFlag") Integer enableFlag,
                                                 @RequestParam("startDate") String startDate,
                                                 @RequestParam("endDate") String endDate);

    /**
     * 访问分组短链接指定时间内监控数据
     *
     * @param gid       分组标识
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 分组短链接监控信息
     */
    @GetMapping("/api/shortlink/v1/stats/group")
    Result<ShortlinkStatsRespDTO> groupStats(@RequestParam("gid") String gid,
                                             @RequestParam("startDate") String startDate,
                                             @RequestParam("endDate") String endDate);

    /**
     * 访问单个短链接指定时间内监控访问记录数据
     *
     * @param lid       短链接标识符
     * @param gid       分组标识
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param current   当前页
     * @param size      一页数据量
     * @return 短链接监控访问记录信息
     */
    @GetMapping("/api/shortlink/v1/stats/access-record")
    Result<Page<ShortlinkStatsAccessRecordRespDTO>> shortlinkStatsAccessRecord(@RequestParam("lid") String lid,
                                                                               @RequestParam("gid") String gid,
                                                                               @RequestParam("startDate") String startDate,
                                                                               @RequestParam("endDate") String endDate,
                                                                               @RequestParam("enableFlag") Integer enableFlag,
                                                                               @RequestParam("current") Long current,
                                                                               @RequestParam("size") Long size);

    /**
     * 访问分组短链接指定时间内监控访问记录数据
     *
     * @param gid       分组标识
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param current   当前页
     * @param size      一页数据量
     * @return 分组短链接监控访问记录信息
     */
    @GetMapping("/api/shortlink/v1/stats/access-record/group")
    Result<Page<ShortlinkStatsAccessRecordRespDTO>> groupStatsAccessRecord(@RequestParam("gid") String gid,
                                                                           @RequestParam("startDate") String startDate,
                                                                           @RequestParam("endDate") String endDate,
                                                                           @RequestParam("current") Long current,
                                                                           @RequestParam("size") Long size);
}
