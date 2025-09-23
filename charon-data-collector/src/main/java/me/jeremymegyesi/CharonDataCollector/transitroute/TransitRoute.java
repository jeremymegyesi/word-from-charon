package me.jeremymegyesi.CharonDataCollector.transitroute;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import me.jeremymegyesi.CharonCommon.transitroute.AbstractTransitRoute;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitSchedule;

@Entity
@Table(name = "transit_route")
@EqualsAndHashCode(callSuper = true)
public class TransitRoute extends AbstractTransitRoute {
    // Additional fields or methods specific to the collector module can be added here

    @OneToMany(mappedBy = "route")
    private List<TransitSchedule> schedules;
}