package me.jeremymegyesi.CharonCommon.transitschedule;

import lombok.Data;

@Data
public class ScheduleData {
    private TerminalScheduleData onwardSchedule;
    private TerminalScheduleData returnSchedule;
}
