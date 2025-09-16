package me.jeremymegyesi.CharonDataCollector.transitschedule;

public interface ScheduleService {
    TransitSchedule getCurrentSchedule(String transitRouteCode);
}
