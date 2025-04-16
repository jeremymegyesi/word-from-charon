package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitScheduleRepository extends JpaRepository<TransitSchedule, UUID> {
    
}
