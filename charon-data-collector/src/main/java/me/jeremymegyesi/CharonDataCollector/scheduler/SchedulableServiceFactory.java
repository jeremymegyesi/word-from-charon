package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SchedulableServiceFactory {
    private final Map<String, SchedulableService> serviceMap;

    public SchedulableService getService(String name) {
        return serviceMap.get(name);
    }
}
