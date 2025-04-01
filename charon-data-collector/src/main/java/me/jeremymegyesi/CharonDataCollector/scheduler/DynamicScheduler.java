package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

public class DynamicScheduler {
    private static final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private static final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    static {
        taskScheduler.initialize();
    }

    public static void scheduleTask(String taskName, String cronExpression, Runnable task) {
        if (scheduledTasks.containsKey(taskName)) {
            cancelTask(taskName);
        }

        ScheduledFuture<?> future = taskScheduler.schedule(task, new CronTrigger(cronExpression));
        scheduledTasks.put(taskName, future);
        System.out.println("Scheduled " + taskName + " with cron: " + cronExpression);
    }

    public static void cancelTask(String taskName) {
        ScheduledFuture<?> future = scheduledTasks.remove(taskName);
        if (future != null) {
            future.cancel(false);
            System.out.println("Cancelled " + taskName);
        }
    }
}
