package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonDataCollector.transitschedule.ScheduleData;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitScheduleRepository;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitTime;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitTimeCondition;

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
			TransitSchedule schedule = scheduleRepository.getOne(UUID.fromString("935c69bc-4313-4838-8175-359903223ce1"));
			log.info("schedule: " + schedule.getScheduleData());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		getScheduleDataFromTables(url);
    }

	private List<ScheduleData> getScheduleDataFromTables(String url) {
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
				// Map<DayOfWeek, TreeSet<TransitTime>> transitTimes = new HashMap<>();

				ScheduleData scheduleData = new ScheduleData();
				scheduleData.setTerminal(tableNames.removeFirst());

				int departureIndex = -1;
				int arrivalIndex = -1;
				DayOfWeek dayOfWeek = null;
				List<TransitTime> transitTimes = new ArrayList<>();

				// iterate through the table rows, might be day header or might be transit time body rows
				for (WebElement child : table.findElements(By.xpath("./*"))) {
					// header row
					String header = extractHeader(child);
					if (header != null) {
						dayOfWeek = convertToDayOfWeek(header);
						departureIndex = getHeaderIndex("depart", child);
						arrivalIndex = getHeaderIndex("arrive", child);
						continue;
					}
					if (departureIndex == -1 || arrivalIndex == -1) {
						log.error("Header indices could not be identified for table: " + scheduleData.getTerminal() 
							+ " at subtable: " + dayOfWeek);
						continue;
					}
					
					// body rows
					transitTimes.addAll(extractTimesForDay(child, departureIndex, arrivalIndex, dayOfWeek));
				}

				SortedSet<TransitTime> transitTimesSet = aggregateTransitTimes(transitTimes);
				scheduleData.setTransitTimes(transitTimesSet);
				scheduleDataList.add(scheduleData);
			}
			// print results
			log.info("SCHEDULE DATA:");
			for (ScheduleData scheduleData : scheduleDataList) {
				for (TransitTime transitTime : scheduleData.getTransitTimes()) {
					log.info(transitTime.toString());
				}
			}
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

	private SortedSet<TransitTime> aggregateTransitTimes(List<TransitTime> transitTimes) {
		SortedSet<TransitTime> aggrTransitTimes = Collections.synchronizedSortedSet(new TreeSet<>((t1, t2) -> t1.getDeparture().compareTo(t2.getDeparture())));
		if (transitTimes == null || transitTimes.isEmpty()) {
			return aggrTransitTimes;
		}

		transitTimes.sort((t1, t2) -> t1.getDeparture().compareTo(t2.getDeparture()));
		for (int i = 0; i < transitTimes.size() - 1; i++) {
			// loop through and merge same times
			int j = i + 1;
			TransitTime t1 = transitTimes.get(i);
			TransitTime t2 = transitTimes.get(j);

			while (j < transitTimes.size() && t1.getDeparture().equals(t2.getDeparture())) {
				List<DayOfWeek> includedDays = new ArrayList<>(java.util.Arrays.asList(DayOfWeek.values()));
				includedDays.removeAll(t2.getExcludedDaysOfWeek());
				t1.getExcludedDaysOfWeek().removeAll(includedDays);
				t1.setTransitTimeConditions(aggregateTransitTimeConditions(t1.getTransitTimeConditions(), t2.getTransitTimeConditions()));
				if (t1.getExcludedDates() == null) {
					t1.setExcludedDates(new TreeSet<>());
				}
				if (t2.getExcludedDates() == null) {
					t2.setExcludedDates(new TreeSet<>());
				}
				// excluded dates are not dependent on day of week
				t1.getExcludedDates().addAll(t2.getExcludedDates());

				transitTimes.remove(j);
				if (j < transitTimes.size()) {
					t2 = transitTimes.get(j);
				}
			}

			aggrTransitTimes.add(t1);
		}
		return aggrTransitTimes;
	}

	private Set<TransitTimeCondition> aggregateTransitTimeConditions(Set<TransitTimeCondition> aggrConditions, Set<TransitTimeCondition> newConditions) {
		if (aggrConditions == null) {
			aggrConditions = new HashSet<>();
		}
		if (newConditions == null) {
			return aggrConditions;
		}
		
		for (TransitTimeCondition newCondition : newConditions) {
			boolean isMerged = false;
			for (TransitTimeCondition aggrCondition : aggrConditions) {
				if (aggrCondition.equalsWithoutDates(newCondition)) {
					// merge conditions
					if (newCondition.getEffectiveDaysOfWeek() != null) {
						if (aggrCondition.getEffectiveDaysOfWeek() == null) {
							aggrCondition.setEffectiveDaysOfWeek(new TreeSet<>());
						}
						aggrCondition.getEffectiveDaysOfWeek().addAll(newCondition.getEffectiveDaysOfWeek());
					}
					if (newCondition.getEffectiveDates() != null) {
						if (aggrCondition.getEffectiveDates() == null) {
							aggrCondition.setEffectiveDates(new TreeSet<>());
						}
						aggrCondition.getEffectiveDates().addAll(newCondition.getEffectiveDates());
					}
					isMerged = true;
					break;
				}
			}
			if (!isMerged) {
				// add new condition
				aggrConditions.add(newCondition);
			}
		}
		
		return aggrConditions;
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

	private List<TransitTime> extractTimesForDay(WebElement body, int departureIndex, int arrivalIndex, DayOfWeek day) {
		if (!body.getTagName().equals("tbody")) {
			return null;
		}

		List<WebElement> timeRows = body.findElements(By.tagName("tr"))
			.stream()
			.filter(row -> row.getAttribute("class").contains("schedule-table-row"))
			.toList();
		List<TransitTime> times = new ArrayList<>();

		for (WebElement row : timeRows) {
			TransitTime newTime = extractTransitTime(row, departureIndex, arrivalIndex, day);
			if (newTime != null) {
				// Set excludedDaysOfWeek as a SortedSet with only the current day removed
				SortedSet<DayOfWeek> excludedDays = new java.util.TreeSet<>(java.util.Arrays.asList(java.time.DayOfWeek.values()));
				excludedDays.remove(day);
				newTime.setExcludedDaysOfWeek(excludedDays);
				times.add(newTime);
			}
		}
		return times;
	}

	private TransitTime extractTransitTime(WebElement row, int departureIndex, int arrivalIndex, DayOfWeek day) {
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
				// conditions & excluded dates stored in the same cell
				TransitTimeCondition condition = extractTransitTimeCondition(cell);
				if (condition != null) {
					condition.setEffectiveDaysOfWeek(Collections.synchronizedSortedSet(new TreeSet<>()));
					condition.getEffectiveDaysOfWeek().add(day);
					transitTime.setTransitTimeConditions(new HashSet<>());
					transitTime.getTransitTimeConditions().add(condition);
				}
				transitTime.setExcludedDates(extractExcludedDates(cell));
			} else if (index == arrivalIndex) {
				transitTime.setArrival(extractTimeFromCell(cell));
			}
			index++;
		}

		if (transitTime.getDeparture() == null || transitTime.getArrival() == null) {
			log.error("Invalid transit time: " + transitTime);
			return null;
		}

		return transitTime;
	}

	private SortedSet<LocalDate> extractExcludedDates(WebElement cell) {
		// excluded dates will appear in a span tag
		try {
			List<WebElement> exclusionElements = cell.findElements(By.tagName("p"));
			java.util.regex.Pattern exceptPattern = java.util.regex.Pattern.compile("^EXCEPT\\s", java.util.regex.Pattern.CASE_INSENSITIVE);
			exclusionElements = exclusionElements.stream()
				.filter(element -> exceptPattern.matcher(element.getText()).find())
				.toList();
			SortedSet<LocalDate> excludedDates = new java.util.TreeSet<>();

			for (WebElement exclusionElement : exclusionElements) {
				String text = exclusionElement.getText();
				if (text != null && !text.isEmpty()) {
					java.util.regex.Pattern monthDayPattern = java.util.regex.Pattern.compile("(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s*\\d{1,2}", java.util.regex.Pattern.CASE_INSENSITIVE);
					java.util.regex.Matcher mdMatcher = monthDayPattern.matcher(text);
					int currentYear = LocalDate.now().getYear();
					while (mdMatcher.find()) {
						String mdString = mdMatcher.group().replaceAll("\\s+", " ").trim();
						DateTimeFormatter mdFormatter = java.time.format.DateTimeFormatter.ofPattern("MMM d", java.util.Locale.ENGLISH);
						try {
							MonthDay monthDay = java.time.MonthDay.parse(mdString, mdFormatter);
							LocalDate date = monthDay.atYear(currentYear);
							excludedDates.add(date);
						} catch (Exception ex) {
							log.warn("Could not parse excluded date: " + mdString, ex);
						}
					}
				}
			}
			if (!excludedDates.isEmpty()) {
				return excludedDates;
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			// most times there will be no excluded dates
			return null;
		}
		return null;
	}

	private TransitTimeCondition extractTransitTimeCondition(WebElement cell) {
		// transit conditions will appear in a p tag
		try {
			List<WebElement> conditionElements = cell.findElements(By.tagName("p"));

			// filter out EXCEPT conditions, will be handled when setting excluded dates
			java.util.regex.Pattern exceptPattern = java.util.regex.Pattern.compile("^EXCEPT\\s", java.util.regex.Pattern.CASE_INSENSITIVE);
			conditionElements = conditionElements.stream()
				.filter(element -> !exceptPattern.matcher(element.getText()).find())
				.toList();
				
			java.util.regex.Pattern noPassengersPattern = java.util.regex.Pattern.compile("NO\\s*PASSENGERS", java.util.regex.Pattern.CASE_INSENSITIVE);
			String conditionText = "";
			Boolean noPassengers = false;
			for (WebElement conditionElement : conditionElements) {
				String text = conditionElement.getText();
				if (noPassengersPattern.matcher(text).find()) {
					noPassengers = true;
				}
				if (!conditionText.isEmpty()) {
					conditionText += "\n";
				}
				conditionText += text;
			}

			if (!conditionText.isEmpty()) {
				TransitTimeCondition condition = new TransitTimeCondition();
				condition.setCondition(conditionText);
				condition.setIsPassengerAllowed(!noPassengers);
				return condition;
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			// most times there will be no condition
			return null;
		}
		return null;
	}

	private LocalTime extractTimeFromCell(WebElement cell) {
		// values wrapped in span sometimes
		try {
			WebElement span = cell.findElement(By.tagName("span"));
			return convertToLocalTime(span.getText());
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return convertToLocalTime(cell.getText());
		}
	}

	private LocalTime convertToLocalTime(String timeText) {
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a");
		// Normalize to uppercase and ensure a single space before AM/PM
		timeText = timeText.trim().toUpperCase();
		if (timeText.endsWith("AM") || timeText.endsWith("PM")) {
			timeText = timeText.replace("AM", " AM").replace("PM", " PM").replaceAll("\\s+", " ").strip();
		}
		return LocalTime.parse(timeText, formatter);
	}

	private DayOfWeek convertToDayOfWeek(String dayText) {
		dayText = dayText.replaceAll("s$", "").toUpperCase();
		try {
			return DayOfWeek.valueOf(dayText);
		} catch (IllegalArgumentException e) {
			log.error("Invalid day of week string: " + dayText);
			return null;
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
