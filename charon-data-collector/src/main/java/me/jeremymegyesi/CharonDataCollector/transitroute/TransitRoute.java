package me.jeremymegyesi.CharonDataCollector.transitroute;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import me.jeremymegyesi.CharonCommon.transitroute.AbstractTransitRoute;

@Entity
@Table(name = "transit_route")
@EqualsAndHashCode(callSuper = true)
public class TransitRoute extends AbstractTransitRoute {
    // Additional fields or methods specific to the collector module can be added here
}