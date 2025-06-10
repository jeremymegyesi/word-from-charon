package me.jeremymegyesi.CharonDataCollector;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import me.jeremymegyesi.CharonDataCollector.scheduler.DynamicSchedulerManager;

@SpringBootApplication
@RequiredArgsConstructor
public class CharonDataCollectorApplication implements CommandLineRunner {
	private final DynamicSchedulerManager schedulerManager;

	// define path to latest ChromeDriver until selenium manager is updated to support it
	@Value("${selenium.chromedriver-path:}")
	private String chromeDriverPath;

	public static void main(String[] args) {
		WebDriverManager.chromedriver().setup(); // Automatically sets up the latest ChromeDriver version
		SpringApplication.run(CharonDataCollectorApplication.class, args);
	}

	public void run(String... args) {
		schedulerManager.scheduleServices();
	}
}
