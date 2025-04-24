package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.UUID;

import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigRepository;

public interface ScheduleScraperExecConfigRepository extends
    ExecutableConfigRepository<ScheduleScraperExecutableConfig, UUID> {
    
}
