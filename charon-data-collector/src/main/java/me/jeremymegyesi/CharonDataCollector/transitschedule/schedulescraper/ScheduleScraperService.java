package me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper;

public interface ScheduleScraperService {
    void scrapeShedule();
    boolean validateSchedule();
    void persistData();
}
