package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonDataCollector.transitschedule.ScheduleData;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitScheduleRepository;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitTime;

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
		}

		try {
			TransitSchedule schedule = scheduleRepository.getOne(UUID.fromString("0a77c75f-00d8-4e7c-b76c-de7f138a6d29"));
			log.info("schedule: " + schedule.getScheduleData());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		getTableData(url);
    }

	private List<ScheduleData> getTableData(String url) {
		log.info("Getting table data...");
		try {
			List<ScheduleData> scheduleDataList = new java.util.ArrayList<>();
			webDriver.get(url);
			List<WebElement> tables = webDriver.findElements(By.cssSelector(".table-seasonal-schedule"))
				.stream()
				.filter(table -> !table.getAttribute("class").contains("schedule-collapse-header"))
				.toList();

			// expecting onward and return tables
			if (tables.size() != 2) {
				log.error("Expected 2 tables, found: " + tables.size());
				return null;
			}

			List<String> tableNames = new ArrayList<String>();
			tableNames.add(webDriver.findElement(By.cssSelector("h2#OnwardSchedule")).getText());
			tableNames.add(webDriver.findElement(By.cssSelector("h2#ReturnSchedule")).getText());

			for (WebElement table : tables) {
				// Each table is a ScheduleData object, should be one for route and one for return route
				String prevDayOfWeek = null;
				Map<String, List<TransitTime>> transitTimes = new HashMap<>();

				ScheduleData scheduleData = new ScheduleData();
				scheduleData.setTerminal(tableNames.removeFirst());

				int departureIndex = -1;
				int arrivalIndex = -1;

				// iterate through the table rows day by day
				for (WebElement child : table.findElements(By.xpath("./*"))) {
					// header row
					String dayOfWeek = extractHeader(child);
					if(dayOfWeek != null) {
						departureIndex = getHeaderIndex("depart", child);
						arrivalIndex = getHeaderIndex("arrive", child);
					}
					// body rows
					List<TransitTime> timesForDay = extractTimesForDay(child, departureIndex, arrivalIndex);
					if (timesForDay != null && departureIndex != -1 && arrivalIndex != -1) {
						transitTimes.put(dayOfWeek, timesForDay);
					}
				}
				scheduleData.setTransitTimes(aggregateTransitTimes(transitTimes));
				scheduleDataList.add(scheduleData);
			}
			log.info("SCHEDULE DATA: " + scheduleDataList);
			return scheduleDataList;
		} catch (Exception e) {
			log.error("An error occurred while getting table data: " + e.getMessage(), e);
		} finally {
			if (webDriver != null) {
				webDriver.quit();
				log.info("WebDriver closed.");
			}
		}
		log.info("Finished getting table data.");
		return null;
	}

	private List<TransitTime> aggregateTransitTimes(Map<String, List<TransitTime>> timesMap) {
		List<String> parsedDays = new ArrayList<String>();
		List<TransitTime> aggrTimes = new ArrayList<>();
		for (Map.Entry<String, List<TransitTime>> entry : timesMap.entrySet()) {
			if (aggrTimes.size() == 0) {
				aggrTimes.addAll(entry.getValue());
				parsedDays.add(entry.getKey());
				continue;
			}
			String day = entry.getKey();
			List<TransitTime> transitTimes = entry.getValue();
			// iterate through aggregated times, see if any are excluded for the current day
			for (TransitTime aggrTransitTime : aggrTimes) {
				if (transitTimes.stream().anyMatch(t -> 
					t.getArrival().equals(aggrTransitTime.getArrival()) && t.getDeparture().equals(aggrTransitTime.getDeparture()))) {
						continue;
				} else {
					aggrTransitTime.getExcludedDaysOfWeek().add(day);
				}
			}
			// add any new times to the aggregated list
			for (TransitTime transitTime : transitTimes) {
				if (aggrTimes.stream().anyMatch(t ->
					t.getArrival().equals(transitTime.getArrival()) && t.getDeparture().equals(transitTime.getDeparture()))) {
					continue;
				} else {
					transitTime.setExcludedDaysOfWeek(parsedDays);
					aggrTimes.add(transitTime);
				}
			}
			// log.info("Processing schedule data for day: " + day);
			// Add logic to process or aggregate transitTimes for the given day
		}
		// aggrTimes.sort((t1, t2) -> t1.getDeparture().compareTo(t2.getDeparture()));
		return aggrTimes;
	}
	
	private String extractHeader(WebElement row) {
		if (!row.getTagName().equals("thead")) {
			return null;
		}
		WebElement headerRow = row.findElement(By.tagName("tr"));
		return headerRow.getAttribute("data-schedule-day");
	}

	private int getHeaderIndex(String headerName, WebElement header) {
		WebElement row = header.findElement(By.tagName("tr"));
		int index = 0;
		for (WebElement headerCell: row.findElements(By.xpath("./*"))){
			if(!headerCell.getTagName().equals("th")) {
				index++;
				continue;
			}
			// sometimes headers have extra junk thrown in, only get last line in that case
			String headerText = headerCell.getText().toLowerCase().strip();
			if (headerText.contains("\n")) {
				headerText = headerText.substring(headerText.lastIndexOf("\n") + 1).strip();
			}
			if (headerText.equals(headerName)) {
				return index;
			}
			index++;
		};
		return -1;
	}

	private List<TransitTime> extractTimesForDay(WebElement body, int departureIndex, int arrivalIndex) {
		if (!body.getTagName().equals("tbody")) {
			return null;
		}
		List<WebElement> timeRows = body.findElements(By.tagName("tr"))
			.stream()
			.filter(row -> row.getAttribute("class").contains("schedule-table-row"))
			.toList();
		List<TransitTime> times = new java.util.ArrayList<>();
		for (WebElement row : timeRows) {
			times.add(extractTransitTime(row, departureIndex, arrivalIndex));
		}
		return times;
	}

	private TransitTime extractTransitTime(WebElement row, int departureIndex, int arrivalIndex) {
		TransitTime transitTime = new TransitTime();
		List<WebElement> cells = row.findElements(By.xpath("./*"));
		int index = 0;

		for(WebElement cell: cells) {
			if (!cell.getTagName().equals("td")) {
				index++;
				continue;
			}
			if (index == departureIndex) {
				transitTime.setDeparture(extractTimeFromCell(cell));
			} else if (index == arrivalIndex) {
				transitTime.setArrival(extractTimeFromCell(cell));
			}
			index++;
		}
		return transitTime;
	}

	private String extractTimeFromCell(WebElement cell) {
		// values wrapped in span sometimes
		try {
			WebElement span = cell.findElement(By.tagName("span"));
			return span.getText();
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return cell.getText();
		}
	}
	
	private void extractSchedule() {
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
