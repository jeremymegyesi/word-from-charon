package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ScheduleScraperParams {
    private String scrapeUrl;
    private String transitRoute;
}
