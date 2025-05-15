package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TransitTime {
    private String departure;
    private String arrival;
    private List<String> excludedDaysOfWeek;
    private List<LocalDateTime> excludedDates;
}
