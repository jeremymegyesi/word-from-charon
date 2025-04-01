package me.jeremymegyesi.CharonDataCollector;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;
import me.jeremymegyesi.CharonDataCollector.scheduler.DynamicSchedulerManager;

@SpringBootApplication
@RequiredArgsConstructor
public class CharonDataCollectorApplication implements CommandLineRunner {
	private final DynamicSchedulerManager schedulerManager;

	public static void main(String[] args) {
		SpringApplication.run(CharonDataCollectorApplication.class, args);
	}

	public void run(String... args) {
		schedulerManager.scheduleServices();
	}
}
