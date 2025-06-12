package me.jeremymegyesi.CharonCommon.transitroute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TransitRouteRepository extends JpaRepository<TransitRoute, UUID> {
}
