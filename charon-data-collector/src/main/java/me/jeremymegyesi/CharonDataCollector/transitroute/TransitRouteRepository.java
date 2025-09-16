package me.jeremymegyesi.CharonDataCollector.transitroute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransitRouteRepository extends JpaRepository<TransitRoute, UUID> {
    TransitRoute findByCode(String code);
}
