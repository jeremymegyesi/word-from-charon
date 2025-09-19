package me.jeremymegyesi.CharonCore.transitmap;

import jakarta.persistence.*;
import lombok.Data;
import me.jeremymegyesi.CharonCore.transitroute.TransitRoute;

@Entity
@Data
@Table(name = "transit_map_config")
public class TransitMapConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "transit_route_id")
    private TransitRoute route;

    @Column(name = "map_type")
    private String mapType;

    @Column(name = "config", columnDefinition = "jsonb")
    private String config;
}