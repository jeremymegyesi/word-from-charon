package me.jeremymegyesi.CharonCommon.kafka;

import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope("prototype")
public class KafkaProducerImpl implements KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, Object event) {
        log.info("Sending schedule update event...");
        kafkaTemplate.send(topic, key, event);
    }
}
