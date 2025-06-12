package me.jeremymegyesi.CharonCommon.transitschedule;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.*;
import lombok.Data;
import me.jeremymegyesi.CharonCommon.transitroute.TransitRoute;;

@Entity
@Data
@Table(name = "transit_schedule")
public class TransitSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Timestamp collectedOn;
    
    @Convert(converter = ScheduleDataJsonConverter.class)
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private ScheduleData scheduleData;

    @OneToOne
    @JoinColumn(name = "transit_route_id")
    private TransitRoute transitRoute;
}
