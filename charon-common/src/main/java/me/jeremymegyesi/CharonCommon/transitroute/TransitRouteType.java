package me.jeremymegyesi.CharonCommon.transitroute;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transit_route_type")
public class TransitRouteType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String type;
}
