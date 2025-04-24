package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.List;

import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigFactory;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigRepository;

public abstract class AbstractSchedulableService<T extends ExecutableConfig, ID> implements SchedulableService<T, ID> {
    protected List<T> configs;
    protected ExecutableConfigFactory<T> configFactory;

    public AbstractSchedulableService(ExecutableConfigFactory<T> configFactory) {
        this.configFactory = configFactory;
        this.configs = configFactory.getConfigs(this.getClass().getSimpleName());
    }

    @Override
    public ExecutableConfigRepository<T, ID> getConfigRepository() {
        return null;
    }
}
