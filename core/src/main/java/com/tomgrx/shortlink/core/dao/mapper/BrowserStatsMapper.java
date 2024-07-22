package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tomgrx.shortlink.core.dao.entity.BrowserStatsDO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * 浏览器统计访问持久层
 */
public interface BrowserStatsMapper extends BaseMapper<BrowserStatsDO> {

    /**
     * 创建或更新浏览器访问监控数据
     */
    @Insert("INSERT INTO " +
            "t_browser_stats (lid, date, cnt, browser, create_time, update_time, delete_flag) " +
            "VALUES( #{linkBrowserStats.lid}, #{linkBrowserStats.date}, #{linkBrowserStats.cnt}, #{linkBrowserStats.browser}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkBrowserStats.cnt};")
    void saveBrowserStats(@Param("linkBrowserStats") BrowserStatsDO browserStatsDO);

    /**
     * 根据短链接获取指定日期内浏览器监控数据
     */
    @Select("SELECT " +
            "    tlbs.browser, " +
            "    SUM(tlbs.cnt) AS count " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_browser_stats tlbs ON tl.lid = tlbs.lid " +
            "WHERE " +
            "    tlbs.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlbs.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlbs.lid, tl.gid, tlbs.browser;")
    List<HashMap<String, Object>> listBrowserStatsByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内浏览器监控数据
     */
    @Select("SELECT " +
            "    tlbs.browser, " +
            "    SUM(tlbs.cnt) AS count " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_browser_stats tlbs ON tl.lid = tlbs.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlbs.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlbs.browser;")
    List<HashMap<String, Object>> listBrowserStatsByGroup(@Param("param") GroupStatsReqDTO requestParam);
}
