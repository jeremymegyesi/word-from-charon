package me.jeremymegyesi.CharonCommon.kafka.events.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.jeremymegyesi.CharonCommon.kafka.events.CustomKafkaEvent;
import me.jeremymegyesi.CharonCommon.transitschedule.AbstractTransitSchedule;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ScheduleUpdatedEvent<T extends AbstractTransitSchedule> extends CustomKafkaEvent {
    public final String eventType = "ScheduleUpdated";
    
    @JsonProperty("updatedSchedule")
    private T updatedSchedule;
}
