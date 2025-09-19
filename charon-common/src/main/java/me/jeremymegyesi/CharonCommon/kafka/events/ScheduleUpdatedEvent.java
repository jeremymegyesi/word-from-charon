package me.jeremymegyesi.CharonCommon.kafka.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jeremymegyesi.CharonCommon.transitschedule.AbstractTransitSchedule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleUpdatedEvent<T extends AbstractTransitSchedule> {
    @JsonProperty("updatedSchedule")
    private T updatedSchedule;
}
