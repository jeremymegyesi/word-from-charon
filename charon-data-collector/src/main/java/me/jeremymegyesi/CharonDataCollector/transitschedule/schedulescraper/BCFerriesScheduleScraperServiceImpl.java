package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitScheduleRepository;

@Slf4j
@Service
public class BCFerriesScheduleScraperServiceImpl extends AbstractScheduleScraperService {
	private final TransitScheduleRepository scheduleRepository;

	public BCFerriesScheduleScraperServiceImpl(TransitScheduleRepository scheduleRepository, ScheduleScraperExecConfigFactory factory) {
		super(factory);
		this.scheduleRepository = scheduleRepository;
	}

	public void scrapeShedule() {
		log.info("Scraping GAB-NAH schedule...");
		String url = "";
		for (ScheduleScraperExecutableConfig config: this.configs) {
			url = config.getScheduleUrl();
			log.info("url: " + url);
		}

		try {
			TransitSchedule schedule = scheduleRepository.getOne(UUID.fromString("0a77c75f-00d8-4e7c-b76c-de7f138a6d29"));
			log.info("schedule: " + schedule.getScheduleData());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		
        webDriver.get(url);
    }

	private void scrapeSite() {
		log.info("Scraping GAB-NAH schedule...");
	}

	public boolean validateSchedule() {
		log.info("validating GAB-NAH schedule...");
        return true;
    }

	public void persistData() {
        log.info("Persisting GAB-NAH schedule...");
    }

    public void testSelenium() {
        log.info("Running main method for schedule scraper");
 
		// navigating to the website 
		webDriver.get("https://www.scrapingcourse.com/ecommerce/"); 
 
		// scraping the title of the website 
		String title = webDriver.getTitle(); 
		log.info("Website title: " + title); 
 
		// closing the web driver 
		webDriver.close(); 
	}
}
