package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;
import me.jeremymegyesi.CharonDataCollector.scheduler.SchedulerConfig.ModuleScheduleConfig;

@Component
@RequiredArgsConstructor
public class DynamicSchedulerManager {
    private final SchedulableServiceFactory schedulableServiceFactory;
    private final SchedulerConfig schedulerConfig;

    // Dynamically schedule tasks based on configuration
    public void scheduleServices() {
        for (Map.Entry<String, ModuleScheduleConfig> moduleEntry : schedulerConfig.getModuleSchedules().entrySet()) {
            // String moduleName = moduleEntry.getKey();
            ModuleScheduleConfig moduleScheduleConfig = moduleEntry.getValue();

            for (Map.Entry<String, String> serviceEntry : moduleScheduleConfig.getServiceCrons().entrySet()) {
                String serviceName = serviceEntry.getKey();
                String cronExpression = serviceEntry.getValue();
                
                cronExpression = resolveCron(cronExpression, moduleScheduleConfig.getDefaultCron(), schedulerConfig.getDefaultCron());

                Runnable task = () -> {
                    SchedulableService<? extends ExecutableConfig, ?> service = schedulableServiceFactory.getService(serviceName);
                    if (service == null) {
                        System.err.println("No service found for: " + serviceName);
                        return;
                    }
                    try {
                        service.executeScheduledTask();
                    } catch (Exception e) {
                        System.err.println("An error occurred while executing scheduled task for: " + service.getClass().getSimpleName());
                    }
                };

                DynamicScheduler.scheduleTask(serviceName, cronExpression, task);
            }


        }
    }

    private String resolveCron(String str, String moduleDefault, String schedulerDefault) {
        if (str.equals("default")) {
            str = moduleDefault;
            if (str.equals("default")) {
                str = schedulerDefault;
            }
        }
        return str;
    }
}
