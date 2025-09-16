package me.jeremymegyesi.CharonCore.transitschedule;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.Map;
import java.util.SortedSet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@RequestMapping("schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
     * Endpoint to retrieve the latest transit schedule.
     *
     * @param transitRoute The transit route identifier for which the schedule is requested.
     * @return A string representation of the latest schedule.
     */
    @GetMapping("/{transitRouteCode}")
    public TransitSchedule getLatestSchedule(@PathVariable String transitRouteCode) {
        return scheduleService.getCurrentSchedule(transitRouteCode);
    }

    @GetMapping("/{transitRouteCode}/next")
    Map<String, SortedSet<LocalTime>> getNextDeparture(@PathVariable String transitRouteCode, @RequestParam(required = false, defaultValue = "1") int count) {
        return scheduleService.getNextDepartureTimes(transitRouteCode, count);
    }
    
}
