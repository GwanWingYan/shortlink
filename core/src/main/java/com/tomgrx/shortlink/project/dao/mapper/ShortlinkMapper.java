package com.tomgrx.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.project.dao.entity.ShortlinkDO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkPageReqDTO;
import com.tomgrx.shortlink.project.dto.req.ShortlinkRecycleBinPageReqDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接持久层
 */
public interface ShortlinkMapper extends BaseMapper<ShortlinkDO> {

    /**
     * 短链接访问统计自增
     */
    void incrementStats(@Param("gid") String gid,
                        @Param("fullShortUrl") String fullShortUrl,
                        @Param("totalPv") Integer totalPv,
                        @Param("totalUv") Integer totalUv,
                        @Param("totalUip") Integer totalUip);

    /**
     * 分页统计短链接
     */
    IPage<ShortlinkDO> pageLink(ShortlinkPageReqDTO requestParam);

    /**
     * 分页统计回收站短链接
     */
    IPage<ShortlinkDO> pageRecycleBinLink(ShortlinkRecycleBinPageReqDTO requestParam);
}
