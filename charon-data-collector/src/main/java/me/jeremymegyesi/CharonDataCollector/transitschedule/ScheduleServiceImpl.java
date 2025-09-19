package me.jeremymegyesi.CharonDataCollector.transitschedule;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final TransitScheduleRepository transitScheduleRepository;

    public TransitSchedule getCurrentSchedule(String transitRouteCode) {
        return transitScheduleRepository.findTopByRoute_CodeOrderByCollectedOnDesc(transitRouteCode);
    }
    
}
