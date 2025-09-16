package me.jeremymegyesi.CharonCommon.kafka.events;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import me.jeremymegyesi.CharonCommon.transitroute.AbstractTransitRoute;
import me.jeremymegyesi.CharonCommon.transitschedule.AbstractTransitSchedule;

@Service
public class ScheduleUpdatedEventJsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ScheduleUpdatedEventJsonConverter() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @SuppressWarnings("unchecked")
    public ScheduleUpdatedEvent<? extends AbstractTransitSchedule<? extends AbstractTransitRoute>> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, ScheduleUpdatedEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not deserialize JSON to ScheduleUpdatedEvent", e);
        }
    }   
}
