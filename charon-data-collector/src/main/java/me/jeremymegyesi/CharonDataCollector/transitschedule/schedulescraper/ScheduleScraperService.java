package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

public interface ScheduleScraperService {
    void scrapeSchedule();
    boolean validateSchedule();
    void persistData();
}
