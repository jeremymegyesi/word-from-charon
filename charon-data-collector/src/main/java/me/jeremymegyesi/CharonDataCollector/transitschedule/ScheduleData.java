package me.jeremymegyesi.CharonDataCollector.transitschedule;

import lombok.Data;

@Data
public class ScheduleData {
    private TerminalScheduleData onwardSchedule;
    private TerminalScheduleData returnSchedule;
}
