package me.jeremymegyesi.CharonCore.transitmap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("map")
public class MapController {
    private final MapService mapService;

    @GetMapping("/{transitRouteCode}")
    public TransitMapConfig getMapConfig(@PathVariable String transitRouteCode) {
        return mapService.getByCode(transitRouteCode);
    }
}
