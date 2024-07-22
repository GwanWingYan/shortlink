package com.tomgrx.shortlink.core.mq.consumer;

import com.alibaba.fastjson2.JSON;
import com.tomgrx.shortlink.core.common.convention.exception.ServiceException;
import com.tomgrx.shortlink.core.dto.biz.StatsRecordDTO;
import com.tomgrx.shortlink.core.mq.idempotent.MessageQueueIdempotentHandler;
import com.tomgrx.shortlink.core.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 基于 Redis Stream 的的统计数据消费者
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStreamStatsConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    private final StatsService statsService;
    private final StringRedisTemplate stringRedisTemplate;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String streamId = message.getStream();
        RecordId recordId = message.getId();

        // 幂等判定，防止重复消费消息
        if (messageQueueIdempotentHandler.isMessageBeingConsumed(recordId.toString())) {
            // 如果消息已执行完成，直接结束
            if (messageQueueIdempotentHandler.isAccomplish(recordId.toString())) {
                return;
            }
            throw new ServiceException("消息未完成流程，需要消息队列重试");
        }

        try {
            // 保存监控记录；若成功，删除消息队列中的消息，表示已经消费
            Map<String, String> producerMap = message.getValue();
            StatsRecordDTO statsRecord = JSON.parseObject(producerMap.get("statsRecord"), StatsRecordDTO.class);
            statsService.saveStatsRecord(statsRecord);
            stringRedisTemplate.opsForStream().delete(Objects.requireNonNull(streamId), recordId.getValue());
        } catch (Throwable ex) {
            // 若失败，删除幂等标识，抛出异常
            messageQueueIdempotentHandler.delMessageProcessed(recordId.toString());
            log.error("记录短链接监控消费异常", ex);
            throw ex;
        }
        // 若成功，将幂等标识设置为“已完成”状态
        messageQueueIdempotentHandler.setAccomplish(recordId.toString());
    }


}
