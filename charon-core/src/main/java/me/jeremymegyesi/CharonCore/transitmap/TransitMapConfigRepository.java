package me.jeremymegyesi.CharonCore.transitmap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitMapConfigRepository extends JpaRepository<TransitMapConfig, String> {
    TransitMapConfig findByRoute_Code(String code);
}
