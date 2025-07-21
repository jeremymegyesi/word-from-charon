package me.jeremymegyesi.CharonCommon.kafka;

public interface KafkaProducer {
    void send(String topic, String key, Object event);
}
