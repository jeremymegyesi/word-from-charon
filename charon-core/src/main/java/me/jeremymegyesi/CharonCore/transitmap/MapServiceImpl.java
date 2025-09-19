package me.jeremymegyesi.CharonCore.transitmap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MapServiceImpl implements MapService {
    private final TransitMapConfigRepository mapConfigRepository;

    public TransitMapConfig getByCode(String transitRouteCode) {
        return mapConfigRepository.findByRoute_Code(transitRouteCode);
    }
    
}
