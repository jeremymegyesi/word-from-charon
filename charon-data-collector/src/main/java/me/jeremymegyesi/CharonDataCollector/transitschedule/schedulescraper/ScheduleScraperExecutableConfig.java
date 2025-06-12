package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;
import me.jeremymegyesi.CharonCommon.transitroute.TransitRoute;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
@Table(name = "transit_schedule_exec_config")
public class ScheduleScraperExecutableConfig extends ExecutableConfig {
    private String scheduleUrl;

    @OneToOne
    @JoinColumn(name = "transit_route_id")
    private TransitRoute transitRoute;
}
