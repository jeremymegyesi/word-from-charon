package me.jeremymegyesi.CharonCore.externalmapping.externalmodels;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.jeremymegyesi.CharonCommon.transitschedule.AbstractTransitSchedule;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransitScheduleDTO extends AbstractTransitSchedule {
    private TransitRouteDTO route;
}
