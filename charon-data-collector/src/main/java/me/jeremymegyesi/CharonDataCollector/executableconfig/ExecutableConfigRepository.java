package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExecutableConfigRepository<T extends ExecutableConfig, ID> extends JpaRepository<T, ID> {
    List<T> findAllByExecServiceClassName(String execServiceClassName);
}
