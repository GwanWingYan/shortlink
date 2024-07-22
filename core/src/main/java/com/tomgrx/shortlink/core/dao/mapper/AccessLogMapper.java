package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tomgrx.shortlink.core.dao.entity.AccessLogDO;
import com.tomgrx.shortlink.core.dao.entity.AccessStatsDO;
import com.tomgrx.shortlink.core.dto.req.GroupAccessRecordReqDTO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访问日志监控持久层
 */
public interface AccessLogMapper extends BaseMapper<AccessLogDO> {

    /**
     * 根据短链接获取指定日期内高频访问IP数据
     */
    @Select("SELECT " +
            "    tlal.ip, " +
            "    COUNT(tlal.ip) AS count " +
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
            "    tlal.lid, tl.gid, tlal.ip " +
            "ORDER BY " +
            "    count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTop5IpByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内高频访问IP数据
     */
    @Select("SELECT " +
            "    tlal.ip, " +
            "    COUNT(tlal.ip) AS count " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_log tlal ON tl.lid = tlal.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlal.ip " +
            "ORDER BY " +
            "    count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTop5IpByGroup(@Param("param") GroupStatsReqDTO requestParam);

    /**
     * 根据短链接获取指定日期内新旧访客数据
     */
    @Select("SELECT " +
            "    SUM(old_visitor) AS oldVisitorCnt, " +
            "    SUM(new_visitor) AS newVisitorCnt " +
            "FROM ( " +
            "    SELECT " +
            "        CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) > 1 THEN 1 ELSE 0 END AS old_visitor, " +
            "        CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) = 1 AND MAX(tlal.create_time) >= #{param.startDate} AND MAX(tlal.create_time) <= #{param.endDate} THEN 1 ELSE 0 END AS new_visitor " +
            "    FROM " +
            "        t_link tl INNER JOIN " +
            "        t_access_log tlal ON tl.lid = tlal.lid " +
            "    WHERE " +
            "        tlal.lid = #{param.lid} " +
            "        AND tl.gid = #{param.gid} " +
            "        AND tl.enable_flag = #{param.enableFlag} " +
            "        AND tl.delete_flag = '0' " +
            "    GROUP BY " +
            "        tlal.vid " +
            ") AS visitor_counts;")
    HashMap<String, Object> findVisitorTypeCntByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 获取短链接用户信息是否新旧访客
     */
    @Select("<script> " +
            "SELECT " +
            "    tlal.vid, " +
            "    CASE " +
            "        WHEN MIN(tlal.create_time) BETWEEN #{startDate} AND #{endDate} THEN '新访客' " +
            "        ELSE '旧访客' " +
            "    END AS visitorType " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_log tlal ON tl.lid = tlal.lid " +
            "WHERE " +
            "    tlal.lid = #{lid} " +
            "    AND tl.gid = #{gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{enableFlag} " +
            "    AND tlal.vid IN " +
            "    <foreach item='item' index='index' collection='vidList' open='(' separator=',' close=')'> " +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    tlal.vid;" +
            "</script>")
    List<Map<String, Object>> selectShortlinkVisitorTypeByVids(
            @Param("gid") String gid,
            @Param("lid") String lid,
            @Param("enableFlag") Integer enableFlag,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("vidList") List<String> vidList
    );

    /**
     * 获取分组用户信息是否新旧访客
     */
    @Select("<script> " +
            "SELECT " +
            "    tlal.vid, " +
            "    CASE " +
            "        WHEN MIN(tlal.create_time) BETWEEN #{startDate} AND #{endDate} THEN '新访客' " +
            "        ELSE '旧访客' " +
            "    END AS visitorType " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_log tlal ON tl.lid = tlal.lid " +
            "WHERE " +
            "    tl.gid = #{gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlal.vid IN " +
            "    <foreach item='item' index='index' collection='vidList' open='(' separator=',' close=')'> " +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    tlal.vid;" +
            "</script>")
    List<Map<String, Object>> selectGroupVisitorTypeByVids(
            @Param("gid") String gid,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("vidList") List<String> vidList
    );

    /**
     * 根据分组获取指定日期内PV、UV、UIP数据
     */
    @Select("SELECT " +
            "    COUNT(tlal.vid) AS pv, " +
            "    COUNT(DISTINCT tlal.vid) AS uv, " +
            "    COUNT(DISTINCT tlal.ip) AS uip " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_access_log tlal ON tl.lid = tlal.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid;")
    AccessStatsDO getTotalAccessStatsByGroup(@Param("param") GroupStatsReqDTO requestParam);

    @Select("SELECT " +
            "    tlal.* " +
            "FROM " +
            "    t_link tl " +
            "    INNER JOIN t_access_log tlal ON tl.lid = tlal.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate} " +
            "ORDER BY " +
            "    tlal.create_time DESC")
    IPage<AccessLogDO> selectGroupPage(@Param("param") GroupAccessRecordReqDTO requestParam);
}
