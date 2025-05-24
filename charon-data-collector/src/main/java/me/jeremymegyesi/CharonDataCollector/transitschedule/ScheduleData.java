package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.util.SortedSet;

import lombok.Data;

@Data
public class ScheduleData {
    private String terminal;
    private SortedSet<TransitTime> transitTimes;
}
