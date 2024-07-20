package com.tomgrx.shortlink.project.mq.producer;

import cn.hutool.core.lang.UUID;
import com.tomgrx.shortlink.project.dto.biz.ShortlinkStatsRecordDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.tomgrx.shortlink.project.common.constant.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;

/**
 * 延迟消费短链接统计发送者
 */
@Component
@Deprecated
@RequiredArgsConstructor
public class DelayShortlinkStatsProducer {

    private final RedissonClient redissonClient;

    /**
     * 发送延迟消费短链接统计
     *
     * @param statsRecord 短链接统计实体参数
     */
    public void send(ShortlinkStatsRecordDTO statsRecord) {
        statsRecord.setKeys(UUID.fastUUID().toString());
        RBlockingDeque<ShortlinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
        RDelayedQueue<ShortlinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(statsRecord, 5, TimeUnit.SECONDS);
    }
}
