package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "charondata.transit-schedule.scraper-config")
public class ScheduleScraperConfigMap {
    private Map<String, ScheduleScraperConfig> scraperMap;
    
    @Data
    public static class ScheduleScraperConfig {
        private ScheduleScraperType type;
        private String url;
        private String executionSchedule;
    }

    public enum ScheduleScraperType {
        FERRY,
        PLANE
    }
}
