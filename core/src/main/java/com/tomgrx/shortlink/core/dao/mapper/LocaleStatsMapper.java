package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tomgrx.shortlink.core.dao.entity.LocaleStatsDO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 地区统计访问持久层
 */
public interface LocaleStatsMapper extends BaseMapper<LocaleStatsDO> {

    /**
     * 创建或更新地区访问监控数据
     */
    @Insert("INSERT INTO " +
            "t_locale_stats (lid, date, cnt, country, province, city, adcode, create_time, update_time, delete_flag) " +
            "VALUES( #{linkLocaleStats.lid}, #{linkLocaleStats.date}, #{linkLocaleStats.cnt}, #{linkLocaleStats.country}, #{linkLocaleStats.province}, #{linkLocaleStats.city}, #{linkLocaleStats.adcode}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkLocaleStats.cnt};")
    void saveLocaleStats(@Param("linkLocaleStats") LocaleStatsDO localeStatsDO);

    /**
     * 根据短链接获取指定日期内地区监控数据
     */
    @Select("SELECT " +
            "    tlls.province, " +
            "    SUM(tlls.cnt) AS cnt " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_locale_stats tlls ON tl.lid = tlls.lid " +
            "WHERE " +
            "    tlls.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlls.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlls.lid, tl.gid, tlls.province;")
    List<LocaleStatsDO> listLocaleByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内地区监控数据
     */
    @Select("SELECT " +
            "    tlls.province, " +
            "    SUM(tlls.cnt) AS cnt " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_locale_stats tlls ON tl.lid = tlls.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlls.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlls.province;")
    List<LocaleStatsDO> listLocaleByGroup(@Param("param") GroupStatsReqDTO requestParam);
}
