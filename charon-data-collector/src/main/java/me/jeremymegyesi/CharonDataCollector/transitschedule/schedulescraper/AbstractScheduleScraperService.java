package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.time.LocalDateTime;
import java.util.UUID;

import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonDataCollector.scheduler.AbstractSchedulableService;
import me.jeremymegyesi.CharonCommon.kafka.events.ScheduleUpdatedEvent;
import me.jeremymegyesi.CharonCommon.transitschedule.ScheduleData;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonCommon.transitschedule.TransitScheduleRepository;
import me.jeremymegyesi.CharonCommon.kafka.KafkaProducer;

@Slf4j
public abstract class AbstractScheduleScraperService extends AbstractSchedulableService<ScheduleScraperExecutableConfig, UUID>
implements ScheduleScraperService {
	protected final WebDriver webDriver;
	protected final TransitScheduleRepository scheduleRepository;
	protected ScheduleData schedule;
	protected ScheduleScraperExecutableConfig currentConfig;
	private final KafkaProducer scheduleKafkaProducer;

	public AbstractScheduleScraperService(TransitScheduleRepository scheduleRepository, ScheduleScraperExecConfigFactory factory, KafkaProducer scheduleKafkaProducer) {
		super(factory);
		ChromeOptions options = new org.openqa.selenium.chrome.ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments("--window-size=1400,600");
		this.webDriver = new ChromeDriver(options);
		this.scheduleRepository = scheduleRepository;
		this.scheduleKafkaProducer = scheduleKafkaProducer;
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

	public boolean validateSchedule() {
		log.info("Validating schedule for config: " + this.currentConfig.getConfigName());
		if (this.schedule == null || this.schedule.getOnwardSchedule() == null || this.schedule.getReturnSchedule() == null) {
			log.error("Schedule data could not be extracted from url: " + this.configs.get(0).getScheduleUrl());
			return false;
		}
		if (this.schedule.getOnwardSchedule().getTransitTimes().isEmpty() || this.schedule.getReturnSchedule().getTransitTimes().isEmpty()) {
			log.error("No transit times found in schedule for config: " + this.currentConfig.getConfigName());
			return false;
		}

		// Check if schedule has changed from the last persisted schedule
		TransitSchedule lastSchedule = scheduleRepository.findTopByTransitRouteOrderByCollectedOnDesc(this.currentConfig.getTransitRoute());
		if (lastSchedule != null && lastSchedule.getScheduleData() != null) {
			ScheduleData lastScheduleData = lastSchedule.getScheduleData();
			if (lastScheduleData.equals(this.schedule)) {
				log.info("No changes detected in schedule for config: " + this.currentConfig.getConfigName());
				return false;
			}
		}

		log.info("Schedule validation successful for config: " + this.currentConfig.getConfigName());
        return true;
    }

	public void persistData() {
        log.info("Persisting schedule for config: " + this.currentConfig.getConfigName());
		if (this.schedule != null && this.schedule.getOnwardSchedule() != null && this.schedule.getReturnSchedule() != null) {
			try {
				TransitSchedule transitSchedule = new TransitSchedule();
				transitSchedule.setCollectedOn(java.sql.Timestamp.valueOf(LocalDateTime.now()));
				transitSchedule.setScheduleData(this.schedule);
				transitSchedule.setTransitRoute(this.currentConfig.getTransitRoute());
				scheduleRepository.save(transitSchedule);
				scheduleKafkaProducer.send("transit-schedule", transitSchedule.getTransitRoute().getRoute().toString(), new ScheduleUpdatedEvent(transitSchedule));
			} catch (Exception e) {
				log.error("Failed to persist schedule data for config {}: " + e.getMessage(), this.currentConfig.getConfigName(), e);
			}
		} else {
			log.warn("No schedule data to persist for config: " + this.currentConfig.getConfigName());
		}
    }
}
