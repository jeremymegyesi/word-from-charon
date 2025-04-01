package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GabNahScheduleScraperServiceImpl extends AbstractScheduleScraperService {
	private String url;

	public void scrapeShedule() {
		log.info("Scraping GAB-NAH schedule...");
		log.info("url: " + url);
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
