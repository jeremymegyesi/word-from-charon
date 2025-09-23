package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutableConfigRepository extends JpaRepository<ExecutableConfig, UUID> {
    List<ExecutableConfig> findAllByExecServiceClassName(String execServiceClassName);
}
