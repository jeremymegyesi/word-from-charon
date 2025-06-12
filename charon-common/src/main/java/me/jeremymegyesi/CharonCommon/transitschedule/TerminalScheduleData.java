package me.jeremymegyesi.CharonCommon.transitschedule;

import java.util.SortedSet;

import lombok.Data;

@Data
public class TerminalScheduleData {
    private String terminal;
    private SortedSet<TransitTime> transitTimes;
}
