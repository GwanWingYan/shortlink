package com.tomgrx.shortlink.core.mq.producer;

import cn.hutool.core.lang.UUID;
import com.tomgrx.shortlink.core.dto.biz.StatsRecordDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.STATS_DELAY_QUEUE_KEY;

/**
 * 基于 redisson 延迟阻塞队列的的统计数据发送者
 */
@Component
@Deprecated
@RequiredArgsConstructor
public class RedisDelayQueueStatsProducer {

    private final RedissonClient redissonClient;

    /**
     * 延迟发送短链接统计数据
     *
     * @param statsRecord 短链接统计实体参数
     */
    public void send(StatsRecordDTO statsRecord) {
        statsRecord.setKeys(UUID.fastUUID().toString());
        RBlockingDeque<StatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(STATS_DELAY_QUEUE_KEY);
        RDelayedQueue<StatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(statsRecord, 5, TimeUnit.SECONDS);
    }
}
