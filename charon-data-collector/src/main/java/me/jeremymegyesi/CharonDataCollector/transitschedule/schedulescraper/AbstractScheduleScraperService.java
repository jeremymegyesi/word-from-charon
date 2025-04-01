package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.chrome.ChromeDriver;

import me.jeremymegyesi.CharonDataCollector.scheduler.SchedulableService;

public abstract class AbstractScheduleScraperService implements ScheduleScraperService, SchedulableService {
	protected WebDriver webDriver;

    public AbstractScheduleScraperService() {
		this.webDriver = new ChromeDriver();
    }

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
