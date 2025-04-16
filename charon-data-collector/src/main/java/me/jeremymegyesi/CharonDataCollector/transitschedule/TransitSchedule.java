package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import me.jeremymegyesi.CharonDataCollector.transitroute.TransitRoute;;

@Entity
@Data
@Table(name = "transit_schedule", schema = "charon_data_collection")
public class TransitSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date collectedOnDate;
    private Object scheduleData;

    @OneToOne
    @JoinColumn(name = "transit_route_id")
    private TransitRoute transitRoute;
}
