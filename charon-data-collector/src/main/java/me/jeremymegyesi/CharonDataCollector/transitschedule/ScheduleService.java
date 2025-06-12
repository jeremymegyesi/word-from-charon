package me.jeremymegyesi.CharonDataCollector.transitschedule;

import me.jeremymegyesi.CharonCommon.transitschedule.TransitSchedule;

public interface ScheduleService {
    TransitSchedule getCurrentSchedule(String transitRouteCode);
}
