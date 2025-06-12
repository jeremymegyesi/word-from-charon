package me.jeremymegyesi.CharonCore.transitschedule;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonCommon.ApiBrokerService;
import me.jeremymegyesi.CharonCommon.transitschedule.TerminalScheduleData;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitScheduleRepository;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final TransitScheduleRepository transitScheduleRepository;
    private final ApiBrokerService apiBrokerService;
    @Value("${charondatasource.port}")
    private String dataPort;
    
    public TransitSchedule getCurrentSchedule(String transitRouteCode) {
        // See if schedule exists for the given transit route ID
        TransitSchedule schedule = transitScheduleRepository.findTopByTransitRoute_RouteOrderByCollectedOnDesc(transitRouteCode);
        if (schedule == null) {
            // Get from charon-data-collector
            log.info("fetching schedule from charon-data-collector for transit route: {}", transitRouteCode);
            try {
                schedule = (TransitSchedule) apiBrokerService.fetchDataFromApi(dataPort, "/schedule/" + transitRouteCode, TransitSchedule.class);
            } catch (Exception e) {
                log.error("Failed to map response to TransitSchedule", e);
                return null;
            }
        }
        return schedule;
    }

    public Map<String, SortedSet<LocalTime>> getNextDepartureTimes(String transitRouteCode, int chunkSize) {
        // Get the current schedule
        TransitSchedule schedule = getCurrentSchedule(transitRouteCode);
        if (schedule == null || schedule.getScheduleData() == null) {
            log.warn("No schedule data available for transit route: {}", transitRouteCode);
            return null;
        }
        
        // Get current time
        LocalTime currentTime = LocalTime.now();
        Map<String, SortedSet<LocalTime>> nextDepartureTimes = new java.util.HashMap<>();
        List<TerminalScheduleData> scheduleDataList = Arrays.asList(
            schedule.getScheduleData().getOnwardSchedule(),
            schedule.getScheduleData().getReturnSchedule()
        );
        
        for (TerminalScheduleData terminalScheduleData : scheduleDataList) {
            String terminal = terminalScheduleData.getTerminal();
            
            TransitTime nextTime = terminalScheduleData.getTransitTimes()
                .stream()
                .filter(tt -> tt.getDeparture().isAfter(currentTime))
                .sorted()
                .findFirst()
                .orElse(null);
            
            if (nextTime != null) {
                SortedSet<TransitTime> nextTimes = terminalScheduleData.getTransitTimes().tailSet(nextTime);
                nextDepartureTimes.put(terminal, nextTimes.stream().limit(chunkSize)
                    .map(TransitTime::getDeparture)
                    .collect(java.util.stream.Collectors.toCollection(java.util.TreeSet::new)));
            } else {
                log.warn("No upcoming departure times found for terminal: {}", terminal);
            }
        }
        return nextDepartureTimes;
    }
}
