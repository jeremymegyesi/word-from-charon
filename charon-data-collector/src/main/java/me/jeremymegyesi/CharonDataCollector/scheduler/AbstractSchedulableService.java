package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.List;

import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigFactory;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigRepository;

public abstract class AbstractSchedulableService<T> implements SchedulableService {
    protected List<ExecutableConfig> configs;
	protected ExecutableConfig currentConfig;
    protected T currentServiceParams;
    protected ExecutableConfigFactory configFactory;
    protected ExecutableConfigRepository configRepository;

    public AbstractSchedulableService(ExecutableConfigFactory configFactory) {
        this.configFactory = configFactory;
        this.configs = configFactory.getConfigs(this.getClass().getSimpleName());
    }

    protected abstract T getServiceParams();
}
