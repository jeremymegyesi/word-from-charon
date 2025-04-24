package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.UUID;

import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.transaction.annotation.Transactional;

import me.jeremymegyesi.CharonDataCollector.scheduler.AbstractSchedulableService;

public abstract class AbstractScheduleScraperService extends AbstractSchedulableService<ScheduleScraperExecutableConfig, UUID>
implements ScheduleScraperService {
	protected WebDriver webDriver;

	public AbstractScheduleScraperService(ScheduleScraperExecConfigFactory factory) {
		super(factory);
		this.webDriver = new ChromeDriver();
	}

	@Transactional
	public void executeScheduledTask() {
		scrapeShedule();
		if (validateSchedule()) {
			persistData();
		}
	}

	public String getCustomExecSchedule() {
		return null;
	}
}
