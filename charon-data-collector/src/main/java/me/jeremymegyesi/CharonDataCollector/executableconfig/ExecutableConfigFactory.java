package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExecutableConfigFactory {

    private final ExecutableConfigRepository configRepository;

    public List<ExecutableConfig> getConfigs(String execServiceClassName) {
        return configRepository.findAllByExecServiceClassName(execServiceClassName);
    }
}
