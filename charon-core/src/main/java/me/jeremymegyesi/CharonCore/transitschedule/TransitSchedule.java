package me.jeremymegyesi.CharonCore.transitschedule;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.jeremymegyesi.CharonCommon.transitschedule.AbstractTransitSchedule;
import me.jeremymegyesi.CharonCore.transitroute.TransitRoute;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TransitSchedule extends AbstractTransitSchedule<TransitRoute> {
    // Additional fields or methods specific to the core module can be added here
    @OneToOne
    @JoinColumn(name = "transit_route_id")
    private TransitRoute route;
}
