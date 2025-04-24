package me.jeremymegyesi.CharonDataCollector.scheduler;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;

@Component
@RequiredArgsConstructor
public class SchedulableServiceFactory {
    private final Map<String, SchedulableService<? extends ExecutableConfig, UUID>> serviceMap;

    public SchedulableService<? extends ExecutableConfig, UUID> getService(String name) {
        String capitalizedName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        return serviceMap.get(capitalizedName);
    }
}
