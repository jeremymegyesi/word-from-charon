package me.jeremymegyesi.CharonDataCollector.transitschedule;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final TransitScheduleRepository transitScheduleRepository;

    public TransitSchedule getCurrentSchedule(String transitRouteCode) {
        return transitScheduleRepository.findTopByTransitRoute_RouteOrderByCollectedOnDesc(transitRouteCode);
    }
    
}
