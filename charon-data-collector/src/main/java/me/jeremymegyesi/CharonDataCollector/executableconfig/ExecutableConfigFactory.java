package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutableConfigFactory<T extends ExecutableConfig> {
    JpaRepository<T, ?> getRepository();
    List<T> getConfigs(String execServiceClassName);
}
