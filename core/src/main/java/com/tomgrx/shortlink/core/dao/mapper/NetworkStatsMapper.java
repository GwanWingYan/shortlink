package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tomgrx.shortlink.core.dao.entity.NetworkStatsDO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 访问网络监控持久层
 */
public interface NetworkStatsMapper extends BaseMapper<NetworkStatsDO> {

    /**
     * 创建或更新网络监控数据
     */
    @Insert("INSERT INTO " +
            "t_network_stats (lid, date, cnt, network, create_time, update_time, delete_flag) " +
            "VALUES( #{networkStats.lid}, #{networkStats.date}, #{networkStats.cnt}, #{networkStats.network}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{networkStats.cnt};")
    void saveNetworkStats(@Param("networkStats") NetworkStatsDO networkStatsDO);

    /**
     * 根据短链接获取指定日期内访问网络监控数据
     */
    @Select("SELECT " +
            "    tlns.network, " +
            "    SUM(tlns.cnt) AS cnt " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_network_stats tlns ON tl.lid = tlns.lid " +
            "WHERE " +
            "    tlns.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlns.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlns.lid, tl.gid, tlns.network;")
    List<NetworkStatsDO> listNetworkStatsByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内访问网络监控数据
     */
    @Select("SELECT " +
            "    tlns.network, " +
            "    SUM(tlns.cnt) AS cnt " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_network_stats tlns ON tl.lid = tlns.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlns.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlns.network;")
    List<NetworkStatsDO> listNetworkStatsByGroup(@Param("param") GroupStatsReqDTO requestParam);
}
