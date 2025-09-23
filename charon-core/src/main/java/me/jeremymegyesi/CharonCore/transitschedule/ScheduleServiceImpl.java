package me.jeremymegyesi.CharonCore.transitschedule;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonCommon.ApiBrokerService;
import me.jeremymegyesi.CharonCommon.kafka.events.schedule.ScheduleUpdatedEvent;
import me.jeremymegyesi.CharonCommon.transitschedule.TerminalScheduleData;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitTime;
import me.jeremymegyesi.CharonCore.externalmapping.converters.TransitScheduleDTOConverter;
import me.jeremymegyesi.CharonCore.externalmapping.externalmodels.TransitScheduleDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final TransitScheduleRepository transitScheduleRepository;
    private final ApiBrokerService apiBrokerService;
    private final TransitScheduleDTOConverter scheduleDTOConverter;

    private final Object lock = new Object();

    @Value("${charondatasource.port}")
    private String dataPort;
    
    public TransitSchedule getCurrentSchedule(String transitRouteCode) {
        // See if schedule exists for the given transit route ID
        TransitSchedule schedule = transitScheduleRepository.findTopByRoute_CodeOrderByCollectedOnDesc(transitRouteCode);
        if (schedule == null) {
            // Get from charon-data-collector
            log.info("fetching schedule from charon-data-collector for transit route: {}", transitRouteCode);
            try {
                TransitScheduleDTO fetchedSchedule = (TransitScheduleDTO) apiBrokerService.fetchDataFromApi(dataPort, "/schedule/" + transitRouteCode, TransitScheduleDTO.class);
                schedule = scheduleDTOConverter.mapToInternalModel(fetchedSchedule);
                saveNewSchedule(schedule);
            } catch (Exception e) {
                log.error("Failed to map response to TransitSchedule", e);
                return null;
            }
        }
        return schedule;
    }

    private void saveNewSchedule(TransitSchedule schedule) {
        synchronized (this.lock) {
            // Make sure schedule does not already exist
            TransitSchedule oldSchedule = transitScheduleRepository.findTopByRoute_CodeOrderByCollectedOnDesc(schedule.getRoute().getCode());
            if (schedule.getScheduleData() != null && oldSchedule != null && oldSchedule.getScheduleData() != null &&
                schedule.getScheduleData().equals(oldSchedule.getScheduleData())) {
                return;
            }
            transitScheduleRepository.save(schedule);
        }
    }

    public Map<String, SortedSet<LocalTime>> getNextDepartureTimes(String transitRouteCode, int chunkSize) {
        // Get the current schedule
        TransitSchedule schedule = getCurrentSchedule(transitRouteCode);
        if (schedule == null || schedule.getScheduleData() == null) {
            log.warn("No schedule data available for transit route: {}", transitRouteCode);
            return null;
        }
        
        // Get current time
        LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0); // Only comparing minutes
        Map<String, SortedSet<LocalTime>> nextDepartureTimes = new java.util.HashMap<>();
        List<TerminalScheduleData> scheduleDataList = Arrays.asList(
            schedule.getScheduleData().getOnwardSchedule(),
            schedule.getScheduleData().getReturnSchedule()
        );
        
        for (TerminalScheduleData terminalScheduleData : scheduleDataList) {
            String terminal = terminalScheduleData.getTerminal();
            
            TransitTime nextTime = terminalScheduleData.getTransitTimes()
                .stream()
                .filter(tt -> tt.getDeparture().isAfter(currentTime) || tt.getDeparture().equals(currentTime))
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

    @Transactional
    public void handleScheduleUpdatedEvent(ScheduleUpdatedEvent<TransitScheduleDTO> event) {
        log.info("Handling schedule updated event for transit route: {}", event.getUpdatedSchedule().getRoute().getCode());
        // Process the updated schedule as needed
        TransitScheduleDTO incoming = event.getUpdatedSchedule();
        TransitSchedule mappedSchedule = scheduleDTOConverter.mapToInternalModel(incoming);

        saveNewSchedule(mappedSchedule);
    }
}
