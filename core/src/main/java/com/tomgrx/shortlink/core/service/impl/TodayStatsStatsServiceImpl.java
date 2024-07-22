package com.tomgrx.shortlink.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomgrx.shortlink.core.dao.entity.TodayStatsDO;
import com.tomgrx.shortlink.core.dao.mapper.TodayStatsMapper;
import com.tomgrx.shortlink.core.service.TodayStatsService;
import org.springframework.stereotype.Service;

/**
 * 短链接今日统计接口实现层
 */
@Service
public class TodayStatsStatsServiceImpl extends ServiceImpl<TodayStatsMapper, TodayStatsDO> implements TodayStatsService {
}