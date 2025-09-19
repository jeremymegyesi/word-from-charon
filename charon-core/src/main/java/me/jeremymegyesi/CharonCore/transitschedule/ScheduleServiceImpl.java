package me.jeremymegyesi.CharonCore.transitschedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonCommon.ApiBrokerService;
import me.jeremymegyesi.CharonCommon.kafka.KafkaConsumer;
import me.jeremymegyesi.CharonCommon.kafka.events.ScheduleUpdatedEvent;
import me.jeremymegyesi.CharonCommon.kafka.events.ScheduleUpdatedEventJsonConverter;
import me.jeremymegyesi.CharonCommon.transitschedule.TerminalScheduleData;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitTime;
import me.jeremymegyesi.CharonCore.transitroute.TransitRouteRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService, KafkaConsumer {
    private final TransitScheduleRepository transitScheduleRepository;
    private final TransitRouteRepository transitRouteRepository;
    private final ApiBrokerService apiBrokerService;
    private final ScheduleUpdatedEventJsonConverter scheduleUpdatedEventJsonConverter;

    @Value("${charondatasource.port}")
    private String dataPort;
    
    public TransitSchedule getCurrentSchedule(String transitRouteCode) {
        // See if schedule exists for the given transit route ID
        TransitSchedule schedule = transitScheduleRepository.findTopByRoute_CodeOrderByCollectedOnDesc(transitRouteCode);
        if (schedule == null) {
            // Get from charon-data-collector
            log.info("fetching schedule from charon-data-collector for transit route: {}", transitRouteCode);
            try {
                TransitSchedule fetchedSchedule = (TransitSchedule) apiBrokerService.fetchDataFromApi(dataPort, "/schedule/" + transitRouteCode, TransitSchedule.class);
                schedule = copySchedule(fetchedSchedule);
                transitScheduleRepository.save(schedule);
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

    @SuppressWarnings("unchecked")
    @Override
    @KafkaListener(id = "transit-schedule-core-consumer", topics = "transit-schedule", groupId = "charon-consumer-group")
    public void listen(Object event) {
        event = ((ConsumerRecord<?, ?>) event).value();
        ScheduleUpdatedEvent<TransitSchedule> scheduleUpdatedEvent =
            (ScheduleUpdatedEvent<TransitSchedule>) scheduleUpdatedEventJsonConverter.convertToEntityAttribute(event.toString());
        this.handleScheduleUpdatedEvent(scheduleUpdatedEvent);
    }

    @Transactional
    private void handleScheduleUpdatedEvent(ScheduleUpdatedEvent<TransitSchedule> event) {
        log.info("Handling schedule updated event for transit route: {}", event.getUpdatedSchedule().getRoute().getCode());
        // Process the updated schedule as needed
        TransitSchedule incoming = event.getUpdatedSchedule();
        TransitSchedule newSchedule = copySchedule(incoming);

        transitScheduleRepository.save(newSchedule);
    }

    private TransitSchedule copySchedule(TransitSchedule original) {
        TransitSchedule copy = new TransitSchedule();
        copy.setCollectedOn(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        copy.setScheduleData(original.getScheduleData());
        copy.setRoute(transitRouteRepository.findByCode(original.getRoute().getCode()));
        return copy;
    }
}
