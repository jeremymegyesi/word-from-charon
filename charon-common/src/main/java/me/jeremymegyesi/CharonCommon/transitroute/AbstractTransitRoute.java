package me.jeremymegyesi.CharonCommon.transitroute;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractTransitRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "route_type_id")
    private TransitRouteType routeType;    
}
