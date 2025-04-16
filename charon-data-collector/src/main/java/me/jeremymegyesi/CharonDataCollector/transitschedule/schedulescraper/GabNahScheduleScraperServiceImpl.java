package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitScheduleRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GabNahScheduleScraperServiceImpl extends AbstractScheduleScraperService {
	private String url;
	private final TransitScheduleRepository scheduleRepository;

	public void scrapeShedule() {
		log.info("Scraping GAB-NAH schedule...");
		log.info("url: " + url);

		try {
			TransitSchedule schedule = scheduleRepository.getOne(UUID.fromString("e2fd8e6e-7bcf-4490-b9d7-f4f60b6e5c16"));
			log.info("schedule: " + schedule.getScheduleData());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		
        // webDriver.get(url);
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
