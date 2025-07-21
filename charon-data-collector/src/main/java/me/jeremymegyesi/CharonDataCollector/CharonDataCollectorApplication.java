package me.jeremymegyesi.CharonDataCollector;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonDataCollector.scheduler.DynamicSchedulerManager;

@SpringBootApplication
@RequiredArgsConstructor
@EntityScan(basePackages = {"me.jeremymegyesi.CharonDataCollector", "me.jeremymegyesi.CharonCommon"})
@EnableJpaRepositories(basePackages = {"me.jeremymegyesi.CharonDataCollector", "me.jeremymegyesi.CharonCommon"})
@ComponentScan(basePackages = {"me.jeremymegyesi.CharonDataCollector", "me.jeremymegyesi.CharonCommon"})
public class CharonDataCollectorApplication implements CommandLineRunner {
	private final DynamicSchedulerManager schedulerManager;

	public static void main(String[] args) {
		WebDriverManager.chromedriver().setup(); // Automatically sets up the latest ChromeDriver version
		SpringApplication.run(CharonDataCollectorApplication.class, args);
	}

	public void run(String... args) {
		schedulerManager.scheduleServices();
	}
}
