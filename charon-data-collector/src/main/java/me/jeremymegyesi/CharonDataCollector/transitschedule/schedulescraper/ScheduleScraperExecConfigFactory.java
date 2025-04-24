package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonDataCollector.executableconfig.AbstractExecutableConfigFactory;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigRepository;

@Component
@RequiredArgsConstructor
public class ScheduleScraperExecConfigFactory extends AbstractExecutableConfigFactory<ScheduleScraperExecutableConfig> {
    private final ScheduleScraperExecConfigRepository repository;
    
    @Override
    public ExecutableConfigRepository<ScheduleScraperExecutableConfig, UUID> getRepository() {
        return repository;
    }
}
