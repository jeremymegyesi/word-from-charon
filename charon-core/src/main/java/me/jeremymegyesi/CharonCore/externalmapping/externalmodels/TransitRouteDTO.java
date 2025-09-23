package me.jeremymegyesi.CharonCore.externalmapping.externalmodels;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.jeremymegyesi.CharonCommon.transitroute.AbstractTransitRoute;

@Data
    @EqualsAndHashCode(callSuper = true)
    public class TransitRouteDTO extends AbstractTransitRoute {
        private List<TransitScheduleDTO> schedules;
    }