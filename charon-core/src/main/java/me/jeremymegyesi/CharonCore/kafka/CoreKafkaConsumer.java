package me.jeremymegyesi.CharonCore.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonCommon.kafka.KafkaConsumer;
import me.jeremymegyesi.CharonCommon.kafka.events.schedule.ScheduleUpdatedEvent;
import me.jeremymegyesi.CharonCore.externalmapping.converters.TransitScheduleDTOConverter;
import me.jeremymegyesi.CharonCore.externalmapping.externalmodels.TransitScheduleDTO;
import me.jeremymegyesi.CharonCore.transitschedule.ScheduleService;

@Component
@Slf4j
public class CoreKafkaConsumer implements KafkaConsumer {

    private final ScheduleService scheduleService;
    private final ObjectMapper objectMapper;

    public CoreKafkaConsumer(ScheduleService scheduleService, ObjectMapper objectMapper, TransitScheduleDTOConverter scheduleDTOConverter) {
        objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper = objectMapper;
        this.scheduleService = scheduleService;
    }

    @SuppressWarnings("unchecked")
    @Override
    @KafkaListener(id = "transit-schedule-core-consumer", topics = "transit-schedule", groupId = "charon-consumer-group")
    public void listen(Object event) {
        event = ((ConsumerRecord<?, ?>) event).value();

        JsonNode root;
        try {
            root = objectMapper.readTree(event.toString());
            if (root.isTextual()) {
                // Unwrap the inner JSON string
                root = objectMapper.readTree(root.asText());
            }
            String eventType = root.get("eventType").asText();


            switch (eventType) {
                case "ScheduleUpdated":
                    event = objectMapper.treeToValue(root, new TypeReference<ScheduleUpdatedEvent<TransitScheduleDTO>>() {});
                    scheduleService.handleScheduleUpdatedEvent((ScheduleUpdatedEvent<TransitScheduleDTO>) event);
                    break;
            
                default:
                    throw new IllegalArgumentException("Unknown message type: " + eventType);
            }
        } catch (JsonProcessingException | NullPointerException e) {
            log.error("Failed to parse kafka event", e);
        }
    }
}
