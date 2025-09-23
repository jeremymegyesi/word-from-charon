package me.jeremymegyesi.CharonCommon.kafka;

import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope("prototype")
public class KafkaProducerImpl implements KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(String topic, String key, Object event) {
        log.info("Sending schedule update event...");
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, key, json);
        } catch (JsonProcessingException e) {
            log.error("Error stringifying kafka event data", e);
        }
    }
}
