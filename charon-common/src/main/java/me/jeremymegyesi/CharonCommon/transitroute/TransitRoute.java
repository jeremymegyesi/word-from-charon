package me.jeremymegyesi.CharonCommon.transitroute;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transit_route")
public class TransitRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String route;

    @ManyToOne
    @JoinColumn(name = "route_type_id")
    private TransitRouteType routeType;    
}
