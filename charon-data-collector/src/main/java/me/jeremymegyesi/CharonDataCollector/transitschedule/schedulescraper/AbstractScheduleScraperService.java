package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.List;
import java.util.UUID;

import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.transaction.annotation.Transactional;

import me.jeremymegyesi.CharonDataCollector.scheduler.AbstractSchedulableService;
import me.jeremymegyesi.CharonDataCollector.transitschedule.ScheduleData;

public abstract class AbstractScheduleScraperService extends AbstractSchedulableService<ScheduleScraperExecutableConfig, UUID>
implements ScheduleScraperService {
	protected WebDriver webDriver;
	protected ScheduleData schedule;
	protected ScheduleScraperExecutableConfig currentConfig;

	public AbstractScheduleScraperService(ScheduleScraperExecConfigFactory factory) {
		super(factory);
		ChromeOptions options = new org.openqa.selenium.chrome.ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments("--window-size=1400,600");
		this.webDriver = new ChromeDriver(options);
	}

	@Transactional
	public void executeScheduledTask() {
		// Iterate through all configs
		for (ScheduleScraperExecutableConfig config : configs) {
				this.currentConfig = config;
				scrapeSchedule();
				if (validateSchedule()) {
					persistData();
				}
		}
	}
}
