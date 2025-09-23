package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.Map;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SchedulableServiceFactory {
    private final Map<String, SchedulableService> serviceMap;

    public SchedulableService getService(String name) {
        String capitalizedName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        return serviceMap.get(capitalizedName);
    }
}
