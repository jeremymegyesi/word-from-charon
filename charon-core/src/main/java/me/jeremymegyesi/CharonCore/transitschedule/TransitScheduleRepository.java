package me.jeremymegyesi.CharonCore.transitschedule;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.jeremymegyesi.CharonCore.transitroute.TransitRoute;

@Repository
public interface TransitScheduleRepository extends JpaRepository<TransitSchedule, UUID> {
    TransitSchedule findTopByRouteOrderByCollectedOnDesc(TransitRoute route);
    TransitSchedule findTopByRoute_CodeOrderByCollectedOnDesc(String routeCode);
}
