package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public abstract class AbstractExecutableConfigFactory<T extends ExecutableConfig> implements ExecutableConfigFactory<T> {
	@Override
	public List<T> getConfigs(String execServiceClassName) {
		System.out.println("Getting configs for: " + execServiceClassName);
		ExecutableConfigRepository<T, ?> repository = getRepository();
		List<T> configs = repository.findAllByExecServiceClassName(execServiceClassName);
		return configs;
	}

	public abstract ExecutableConfigRepository<T, ?> getRepository();
}
