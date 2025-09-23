package me.jeremymegyesi.CharonCore.externalmapping.converters;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonCore.externalmapping.externalmodels.TransitScheduleDTO;
import me.jeremymegyesi.CharonCore.transitroute.TransitRouteRepository;
import me.jeremymegyesi.CharonCore.transitschedule.TransitSchedule;

@Component
@RequiredArgsConstructor
public class TransitScheduleDTOConverter implements DTOConverter<TransitSchedule, TransitScheduleDTO> {
    
    private final TransitRouteRepository routeRepository;

    @Override
    public TransitSchedule mapToInternalModel(TransitScheduleDTO externalModel) {
        TransitSchedule mappedSchedule = new TransitSchedule();
        mappedSchedule.setCollectedOn(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        mappedSchedule.setScheduleData(externalModel.getScheduleData());
        mappedSchedule.setRoute(routeRepository.findByCode(externalModel.getRoute().getCode()));
        return mappedSchedule;
    }
    
}
