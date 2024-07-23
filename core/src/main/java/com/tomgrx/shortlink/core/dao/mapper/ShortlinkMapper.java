package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.core.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.core.dto.req.RecycleBinPageQueryReqDTO;
import com.tomgrx.shortlink.core.dto.resp.ShortlinkPageRespDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接持久层
 */
public interface ShortlinkMapper extends BaseMapper<ShortlinkDO> {

    /**
     * 短链接访问统计自增
     */
    void incrementStats(@Param("gid") String gid,
                        @Param("lid") String lid,
                        @Param("totalPv") Integer totalPv,
                        @Param("totalUv") Integer totalUv,
                        @Param("totalUip") Integer totalUip);

    /**
     * 分页查询有效短链接
     */
    IPage<ShortlinkPageRespDTO> pageLink(ShortlinkPageReqDTO requestParam);

    /**
     * 分页查询回收站中的短链接
     */
    IPage<ShortlinkPageRespDTO> pageRecycleBinLink(RecycleBinPageQueryReqDTO requestParam);
}
