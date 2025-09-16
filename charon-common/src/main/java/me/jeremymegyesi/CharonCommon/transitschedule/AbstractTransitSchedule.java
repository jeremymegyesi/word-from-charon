package me.jeremymegyesi.CharonCommon.transitschedule;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.*;
import lombok.Data;
import me.jeremymegyesi.CharonCommon.transitroute.AbstractTransitRoute;

@Data
@MappedSuperclass
public abstract class AbstractTransitSchedule<T extends AbstractTransitRoute> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Timestamp collectedOn;
    
    @Convert(converter = ScheduleDataJsonConverter.class)
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private ScheduleData scheduleData;

    @OneToOne
    @JoinColumn(name = "transit_route_id", insertable = false, updatable = false)
    private T transitRoute;
}
