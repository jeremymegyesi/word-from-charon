package me.jeremymegyesi.CharonCore.transitroute;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RouteServiceImpl implements RouteService {
    private final TransitRouteRepository routeRepository;

    public List<TransitRoute> getAll(String type) {
        // Implementation to fetch all routes, possibly filtering by type
        return routeRepository.findAll(); // Placeholder return
    }
}
