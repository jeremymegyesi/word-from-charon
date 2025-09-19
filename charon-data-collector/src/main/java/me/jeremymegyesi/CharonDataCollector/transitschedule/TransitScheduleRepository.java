package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.jeremymegyesi.CharonDataCollector.transitroute.TransitRoute;

@Repository
public interface TransitScheduleRepository extends JpaRepository<TransitSchedule, UUID> {
    TransitSchedule findTopByRouteOrderByCollectedOnDesc(TransitRoute transitRoute);
    TransitSchedule findTopByRoute_CodeOrderByCollectedOnDesc(String routeCode);
}
