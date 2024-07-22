package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tomgrx.shortlink.core.dao.entity.TodayStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接今日统计持久层
 */
public interface TodayStatsMapper extends BaseMapper<TodayStatsDO> {

    /**
     * 记录今日统计监控数据
     */
    @Insert("INSERT INTO " +
            "t_today_stats (lid, date,  today_uv, today_pv, today_uip, create_time, update_time, delete_flag) " +
            "VALUES( #{todayStats.lid}, #{todayStats.date}, #{todayStats.todayUv}, #{todayStats.todayPv}, #{todayStats.todayUip}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE today_uv = today_uv +  #{todayStats.todayUv}, today_pv = today_pv +  #{todayStats.todayPv}, today_uip = today_uip +  #{todayStats.todayUip};")
    void saveTodayStats(@Param("todayStats") TodayStatsDO todayStatsDO);
}
