package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "charondata.scheduler")
public class SchedulerConfig {
    private String defaultCron;
    private Map<String, ModuleScheduleConfig> moduleSchedules;

    @Data
    public static class ModuleScheduleConfig {
        private String defaultCron;
        private Map<String, String> serviceCrons;
    }
}
