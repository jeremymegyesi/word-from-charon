package me.jeremymegyesi.CharonCommon.kafka.events;

import lombok.Data;

@Data
public abstract class CustomKafkaEvent {
    public String eventType;
}
