package me.jeremymegyesi.CharonCommon.transitschedule;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractTransitSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Timestamp collectedOn;
    
    @Convert(converter = ScheduleDataJsonConverter.class)
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private ScheduleData scheduleData;
}
