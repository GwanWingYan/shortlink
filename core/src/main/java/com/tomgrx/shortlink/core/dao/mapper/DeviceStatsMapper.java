package com.tomgrx.shortlink.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tomgrx.shortlink.core.dao.entity.DeviceStatsDO;
import com.tomgrx.shortlink.core.dto.req.GroupStatsReqDTO;
import com.tomgrx.shortlink.core.dto.req.ShortlinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 访问设备监控持久层
 */
public interface DeviceStatsMapper extends BaseMapper<DeviceStatsDO> {

    /**
     * 创建或更新访问设备监控数据
     */
    @Insert("INSERT INTO " +
            "t_device_stats (lid, date, cnt, device, create_time, update_time, delete_flag) " +
            "VALUES( #{deviceStats.lid}, #{deviceStats.date}, #{deviceStats.cnt}, #{deviceStats.device}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{deviceStats.cnt};")
    void saveDeviceStats(@Param("deviceStats") DeviceStatsDO deviceStatsDO);

    /**
     * 根据短链接获取指定日期内访问设备监控数据
     */
    @Select("SELECT " +
            "    tlds.device, " +
            "    SUM(tlds.cnt) AS cnt " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_device_stats tlds ON tl.lid = tlds.lid " +
            "WHERE " +
            "    tlds.lid = #{param.lid} " +
            "    AND tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = #{param.enableFlag} " +
            "    AND tlds.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tlds.lid, tl.gid, tlds.device;")
    List<DeviceStatsDO> listDeviceStatsByShortlink(@Param("param") ShortlinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内访问设备监控数据
     */
    @Select("SELECT " +
            "    tlds.device, " +
            "    SUM(tlds.cnt) AS cnt " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_device_stats tlds ON tl.lid = tlds.lid " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.delete_flag = '0' " +
            "    AND tl.enable_flag = '0' " +
            "    AND tlds.date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid, tlds.device;")
    List<DeviceStatsDO> listDeviceStatsByGroup(@Param("param") GroupStatsReqDTO requestParam);
}
