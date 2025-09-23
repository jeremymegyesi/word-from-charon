package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonDataCollector.scheduler.SchedulerConfig.ModuleScheduleConfig;

@Slf4j
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
                    SchedulableService service = schedulableServiceFactory.getService(serviceName);
                    if (service == null) {
                        log.error("No service found for: " + serviceName);
                        return;
                    }
                    try {
                        service.executeScheduledTask();
                    } catch (Exception e) {
                        log.error("An error occurred while executing scheduled task for: " + service.getClass().getSimpleName() + e.getMessage(), e);
                    }
                };

                // Run the task immediately in addition to scheduling it
                new Thread(task).start();
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
