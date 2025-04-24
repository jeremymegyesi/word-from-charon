package me.jeremymegyesi.CharonDataCollector.scheduler;

import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigRepository;

public interface SchedulableService<T extends ExecutableConfig, ID> {
    void executeScheduledTask();
    ExecutableConfigRepository<T, ID> getConfigRepository();
}
