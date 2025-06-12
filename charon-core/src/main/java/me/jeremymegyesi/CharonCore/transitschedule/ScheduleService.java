package me.jeremymegyesi.CharonCore.transitschedule;

import java.time.LocalTime;
import java.util.Map;
import java.util.SortedSet;

import me.jeremymegyesi.CharonCommon.transitschedule.TransitSchedule;

public interface ScheduleService {
    /**
     * Retrieves the current schedule for a given transit route.
     *
     * @param transitRouteCode The identifier code of the transit route.
     * @return The current schedule for the specified transit route.
     */
    TransitSchedule getCurrentSchedule(String transitRouteCode);

    Map<String, SortedSet<LocalTime>> getNextDepartureTimes(String transitRouteCode, int chunkSize);
}
