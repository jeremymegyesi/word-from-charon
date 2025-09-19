package me.jeremymegyesi.CharonCore.transitroute;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@RequestMapping("routes")
public class RouteController {
    private final RouteService routeService;

    /**
     * Base endpoint returns list of all monitored routes.
     *
     * @return All transit routes.
     */
    @GetMapping("")
    List<TransitRoute> getAllRoutes(@RequestParam(required = false) String type) {
        return routeService.getAll(type);
    }

    @GetMapping("/{transitRouteCode}")
    public TransitRoute getRoute(@PathVariable String transitRouteCode) {
        return routeService.getByCode(transitRouteCode);
    }
    
}
