import { useEffect, useRef, useState } from 'react';
import RouteList from './RouteList';
import axios from 'axios';
import { TransitRoute } from '../route.types';
import { fetchDeparturesEnrichRoutes, fetchRoutesEnrichWithDepartures } from "../route.utils";
import { sanitizeRouteSlug } from '../route.utils';
import { useNavigate } from 'react-router-dom';
import { isPastTime } from '../route-insights/RouteInsightsPage.utils';

type RLTransitRoute = Partial<TransitRoute> & {
    code: string;
    departures: {
        fromOrigin: string[],
        fromDestination: string[]
    }
}

const RouteListContainer: React.FC = () => {
    const [enrichedRoutes, setEnrichedRoutes] = useState<RLTransitRoute[]>([]);
    const routesRef = useRef(enrichedRoutes);
    const [routeMap, setRouteMap] = useState<Map<string, RLTransitRoute>>();
    const routeMapRef = useRef(routeMap);
    const [loading, setLoading] = useState(true);

    const [currentTime, setCurrentTime] = useState(new Date());
    const timeRef = useRef(currentTime);

    const api = axios.create({
        // TODO: update to env var
        baseURL: 'http://localhost:7000'
    });

    // keep timeRef updated
    useEffect(() => {
        timeRef.current = currentTime;
    }, [currentTime]);

    // keep routesRef & routeMapRef updated when routes change
    useEffect(() => {
        routesRef.current = enrichedRoutes;
        const updatedMap = new Map(routesRef.current.map(route => [route.code, route]));
        setRouteMap(updatedMap);
        routeMapRef.current = updatedMap;
    }, [enrichedRoutes]);

    // fetch route data and enrich with schedule info
    useEffect(() => {
        const fetchAndEnrichRoutes = async () => {
            setLoading(true);
            try {
                const res = await fetchRoutesEnrichWithDepartures(api) as RLTransitRoute[];
                setEnrichedRoutes(res);
            } catch (error) {
                console.error('Failed to fetch routes:', error);
                setEnrichedRoutes([]);
            }
            setLoading(false);
        };

        fetchAndEnrichRoutes();
    }, []);

    const navigate = useNavigate();
    const handleRouteSelect = (route: TransitRoute) => {
        const slug = sanitizeRouteSlug(route.code);
        navigate(`/${slug}/insights`);
    };

    // update route with up-to-date departures
    useEffect(() => {
        const interval = setInterval(() => {
            const curr_time = new Date();
            setCurrentTime(curr_time);
            timeRef.current = curr_time;

            const refreshRouteTimes = async (routes: RLTransitRoute[]) => {
                try {
                    if (!routesRef.current) {
                        return;
                    }
                    const res = await fetchDeparturesEnrichRoutes(api, routes as TransitRoute[]) as RLTransitRoute[];

                    let _routeMap = routeMapRef.current as Map<string, RLTransitRoute>;
                    res.forEach(route => _routeMap?.set(route.code, route));
                    const updatedRoutes = Array.from(_routeMap.values());
                    setEnrichedRoutes(updatedRoutes);
                } catch (error) {
                    console.error('Failed to refresh departure times:', error);
                    return;
                }
            }
                
            // Iterate through routes and update any with out-of-date departures
            if (routeMapRef && routeMapRef.current && routeMapRef.current.size > 0 && timeRef && timeRef.current) { 
                const _routeMap = routeMapRef.current;
                
                const refreshRoutes = [] as RLTransitRoute[];
                
                _routeMap.forEach((route: RLTransitRoute) => {
                    const requiresRefresh =
                        [route.departures.fromOrigin, route.departures.fromDestination]
                        .some(departures => departures.some(dep_time => isPastTime(dep_time, timeRef.current)));
                    if (requiresRefresh) {
                        refreshRoutes.push(route);
                    }
                });

                if (refreshRoutes.length > 0) {
                    refreshRouteTimes(refreshRoutes);
                }
            }
        }, 60000);

        return () => clearInterval(interval);
    }, []);

    return (
        <RouteList loading={loading} routes={enrichedRoutes} handleRouteSelect={handleRouteSelect} />
    );
};

export default RouteListContainer;