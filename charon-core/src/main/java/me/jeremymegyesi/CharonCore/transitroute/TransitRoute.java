package me.jeremymegyesi.CharonCore.transitroute;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.jeremymegyesi.CharonCommon.transitroute.AbstractTransitRoute;
import me.jeremymegyesi.CharonCore.transitschedule.TransitSchedule;

@Entity
@Data
@Table(name = "transit_route")
@EqualsAndHashCode(callSuper = true)
public class TransitRoute extends AbstractTransitRoute {
    // Additional fields or methods specific to the core module can be added here
    @Column(name = "display_name")
    private String displayName;

    @Column(name = "from_location")
    private String fromLocation;

    @Column(name = "to_location")
    private String toLocation;

    @OneToOne(mappedBy = "route")
    private TransitSchedule schedule;
}
