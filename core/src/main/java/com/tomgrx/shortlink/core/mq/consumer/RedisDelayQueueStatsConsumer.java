package com.tomgrx.shortlink.core.mq.consumer;

import com.tomgrx.shortlink.core.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.core.dto.biz.StatsRecordDTO;
import com.tomgrx.shortlink.core.mq.idempotent.MessageQueueIdempotentHandler;
import com.tomgrx.shortlink.core.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import static com.tomgrx.shortlink.constant.RedisKeyConstant.STATS_DELAY_QUEUE_KEY;

/**
 * 基于 redisson 延迟阻塞队列的的统计数据消费者
 */
@Deprecated
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisDelayQueueStatsConsumer implements InitializingBean {

    private final StatsService statsService;
    private final RedissonClient redissonClient;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    public void onMessage() {
        Executors.newSingleThreadExecutor(
                        runnable -> {
                            Thread thread = new Thread(runnable);
                            thread.setName("redis_delay_queue_stats_consumer");
                            thread.setDaemon(Boolean.TRUE);
                            return thread;
                        })
                .execute(() -> {
                    RBlockingDeque<StatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(STATS_DELAY_QUEUE_KEY);
                    RDelayedQueue<StatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
                    for (; ; ) {
                        try {
                            StatsRecordDTO statsRecord = delayedQueue.poll();
                            if (statsRecord != null) {
                                // 幂等判定，防止重复消费消息
                                if (messageQueueIdempotentHandler.isMessageBeingConsumed(statsRecord.getKeys())) {
                                    // 如果消息已执行完成，直接结束
                                    if (messageQueueIdempotentHandler.isAccomplish(statsRecord.getKeys())) {
                                        return;
                                    }
                                    throw new ServiceException("消息未完成流程，需要消息队列重试");
                                }
                                // 保存监控记录
                                try {
                                    statsService.saveStatsRecord(statsRecord);
                                } catch (Throwable ex) {
                                    messageQueueIdempotentHandler.delMessageProcessed(statsRecord.getKeys());
                                    log.error("延迟记录短链接监控消费异常", ex);
                                }
                                messageQueueIdempotentHandler.setAccomplish(statsRecord.getKeys());
                                continue;
                            }
                            LockSupport.parkUntil(500);
                        } catch (Throwable ignored) {
                        }
                    }
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        onMessage();
    }
}