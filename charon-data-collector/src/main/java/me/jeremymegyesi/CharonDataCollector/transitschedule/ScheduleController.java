package me.jeremymegyesi.CharonDataCollector.transitschedule;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitSchedule;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


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
}
