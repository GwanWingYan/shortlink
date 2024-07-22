package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tomgrx.shortlink.core.dao.entity.AccessStatsDO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 短链接基础访问监控持久层
 */
public interface AccessStatsMapper extends BaseMapper<AccessStatsDO> {

    /**
     * 创建或更新基础访问监控数据
     */
    @Insert("INSERT INTO " +
            "t_access_stats (lid, date, pv, uv, uip, hour, weekday, create_time, update_time, delete_flag) " +
            "VALUES( #{linkAccessStats.lid}, #{linkAccessStats.date}, #{linkAccessStats.pv}, #{linkAccessStats.uv}, #{linkAccessStats.uip}, #{linkAccessStats.hour}, #{linkAccessStats.weekday}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE pv = pv +  #{linkAccessStats.pv}, uv = uv + #{linkAccessStats.uv}, uip = uip + #{linkAccessStats.uip};")
    void saveAccessStats(@Param("linkAccessStats") AccessStatsDO accessStatsDO);

    /**
     * 获取指定日期内短链接每天的 PV, UV, UIP 数据
     */
    @Select("SELECT " +
            "    tlas.date, " +
            "    SUM(tlas.pv) AS pv, " +
            "    SUM(tlas.uv) AS uv, " +
            "    SUM(tlas.uip) AS uip " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_stats tlas ON tl.lid = tlas.lid " +
            "WHERE " +
            "    tlas.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlas.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlas.lid, tl.gid, tlas.date;")
    List<AccessStatsDO> listDailyAccessStatsByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 获取指定日期内短链接总体的 PV、UV、UIP 数据
     */
    @Select("SELECT " +
            "    COUNT(tlal.user) AS pv, " +
            "    COUNT(DISTINCT tlal.user) AS uv, " +
            "    COUNT(DISTINCT tlal.ip) AS uip " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_log tlal ON tl.lid = tlal.lid " +
            "WHERE " +
            "    tlal.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlal.lid, tl.gid;")
    AccessStatsDO getTotalAccessStatsByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内基础监控数据
     */
    @Select("SELECT " +
            "    tlas.date, " +
            "    SUM(tlas.pv) AS pv, " +
            "    SUM(tlas.uv) AS uv, " +
            "    SUM(tlas.uip) AS uip " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_stats tlas ON tl.lid = tlas.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlas.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlas.date;")
    List<AccessStatsDO> listDailyAccessStatsByGroup(@Param("param") GroupStatsReqDTO requestParam);

    /**
     * 根据短链接获取指定日期内小时基础监控数据
     */
    @Select("SELECT " +
            "    tlas.hour, " +
            "    SUM(tlas.pv) AS pv " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_stats tlas ON tl.lid = tlas.lid " +
            "WHERE " +
            "    tlas.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlas.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlas.lid, tl.gid, tlas.hour;")
    List<AccessStatsDO> listHourStatsByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内小时基础监控数据
     */
    @Select("SELECT " +
            "    tlas.hour, " +
            "    SUM(tlas.pv) AS pv " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_stats tlas ON tl.lid = tlas.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlas.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlas.hour;")
    List<AccessStatsDO> listHourStatsByGroup(@Param("param") GroupStatsReqDTO requestParam);

    /**
     * 根据短链接获取指定日期内小时基础监控数据
     */
    @Select("SELECT " +
            "    tlas.weekday, " +
            "    SUM(tlas.pv) AS pv " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_stats tlas ON tl.lid = tlas.lid " +
            "WHERE " +
            "    tlas.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlas.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlas.lid, tl.gid, tlas.weekday;")
    List<AccessStatsDO> listWeekdayStatsByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内小时基础监控数据
     */
    @Select("SELECT " +
            "    tlas.weekday, " +
            "    SUM(tlas.pv) AS pv " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_stats tlas ON tl.lid = tlas.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlas.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlas.weekday;")
    List<AccessStatsDO> listWeekdayStatsByGroup(@Param("param") GroupStatsReqDTO requestParam);
}
