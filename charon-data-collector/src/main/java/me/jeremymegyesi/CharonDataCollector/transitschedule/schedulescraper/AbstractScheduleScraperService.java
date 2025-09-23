package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfig;
import me.jeremymegyesi.CharonDataCollector.executableconfig.ExecutableConfigFactory;
import me.jeremymegyesi.CharonDataCollector.scheduler.AbstractSchedulableService;
import me.jeremymegyesi.CharonDataCollector.transitroute.TransitRouteRepository;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitSchedule;
import me.jeremymegyesi.CharonDataCollector.transitschedule.TransitScheduleRepository;
import me.jeremymegyesi.CharonCommon.transitschedule.ScheduleData;
import me.jeremymegyesi.CharonCommon.kafka.KafkaProducer;
import me.jeremymegyesi.CharonCommon.kafka.events.schedule.ScheduleUpdatedEvent;

@Slf4j
public abstract class AbstractScheduleScraperService extends AbstractSchedulableService<ScheduleScraperParams> implements ScheduleScraperService {
	protected final WebDriver webDriver;
	protected final TransitScheduleRepository scheduleRepository;
	protected final TransitRouteRepository routeRepository;
	protected ScheduleData schedule;
	private final KafkaProducer scheduleKafkaProducer;

	public AbstractScheduleScraperService(TransitScheduleRepository scheduleRepository, TransitRouteRepository routeRepository, ExecutableConfigFactory configFactory, KafkaProducer scheduleKafkaProducer) {
		super(configFactory);

		ChromeOptions options = new org.openqa.selenium.chrome.ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments("--window-size=1400,600");
		this.webDriver = new ChromeDriver(options);
		this.scheduleRepository = scheduleRepository;
		this.routeRepository = routeRepository;
		this.scheduleKafkaProducer = scheduleKafkaProducer;
	}

	protected ScheduleScraperParams getServiceParams() {
		try {
			return (ScheduleScraperParams) this.configs.get(0).getParams().getValue();
		} catch (ClassCastException e) {
			log.error("Received JSON value which could not be mapped to type ScheduleScraperParams", e);
			return null;
		}
	}

	@Transactional
	public void executeScheduledTask() {
		// Iterate through all configs
		for (ExecutableConfig config : configs) {
				this.currentConfig = config;
				this.currentServiceParams = getServiceParams();
				if (this.currentServiceParams == null) {
					return;
				}

				scrapeSchedule();
				if (validateSchedule()) {
					persistData();
				}
		}
	}

	public boolean validateSchedule() {
		log.info("Validating schedule for config: " + this.currentConfig.getConfigName());

		ScheduleScraperParams scraperParams = this.getServiceParams();
		if (scraperParams == null) {
			return false;
		}

		if (this.schedule == null || this.schedule.getOnwardSchedule() == null || this.schedule.getReturnSchedule() == null) {
			log.error("Schedule data could not be extracted from url: " + scraperParams.getScrapeUrl());
			return false;
		}
		if (this.schedule.getOnwardSchedule().getTransitTimes().isEmpty() || this.schedule.getReturnSchedule().getTransitTimes().isEmpty()) {
			log.error("No transit times found in schedule for config: " + this.currentConfig.getConfigName());
			return false;
		}

		// Check if schedule has changed from the last persisted schedule
		TransitSchedule lastSchedule = scheduleRepository.findTopByRoute_CodeOrderByCollectedOnDesc(scraperParams.getTransitRoute());
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

		ScheduleScraperParams scraperParams = this.getServiceParams();
		if (scraperParams == null) {
			return;
		}

		if (this.schedule != null && this.schedule.getOnwardSchedule() != null && this.schedule.getReturnSchedule() != null) {
			try {
				TransitSchedule transitSchedule = new TransitSchedule();
				transitSchedule.setCollectedOn(java.sql.Timestamp.valueOf(LocalDateTime.now()));
				transitSchedule.setScheduleData(this.schedule);
				transitSchedule.setRoute(routeRepository.findByCode(scraperParams.getTransitRoute()));
				scheduleRepository.save(transitSchedule);
				scheduleKafkaProducer.send("transit-schedule", transitSchedule.getRoute().getCode().toString(), new ScheduleUpdatedEvent<>(transitSchedule));
			} catch (Exception e) {
				log.error("Failed to persist schedule data for config {}: " + e.getMessage(), this.currentConfig.getConfigName(), e);
			}
		} else {
			log.warn("No schedule data to persist for config: " + this.currentConfig.getConfigName());
		}
    }
}
