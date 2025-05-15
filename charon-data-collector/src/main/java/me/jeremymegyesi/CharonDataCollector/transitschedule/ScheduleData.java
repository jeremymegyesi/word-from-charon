package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleData {
    private String terminal;
    private List<TransitTime> transitTimes;
}
