package me.jeremymegyesi.CharonCore.transitroute;

import java.util.List;

public interface RouteService {
    public List<TransitRoute> getAll(String type);
    
    public TransitRoute getByCode(String transitRouteCode);
}
