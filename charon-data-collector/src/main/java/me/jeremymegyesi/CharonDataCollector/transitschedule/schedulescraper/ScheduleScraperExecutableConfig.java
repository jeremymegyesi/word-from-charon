package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;
import me.jeremymegyesi.CharonDataCollector.transitroute.TransitRoute;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "transit_schedule_exec_config")
public class ScheduleScraperExecutableConfig extends ExecutableConfig {
    private String scheduleUrl;

    @OneToOne
    @JoinColumn(name = "transit_route_id")
    private TransitRoute transitRoute;
}
