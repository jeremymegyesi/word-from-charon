import { useNavigate, useParams } from "react-router-dom";
import { fetchRouteBySlug, isPastTime } from './RouteInsightsPage.utils';
import axios from "axios";
import RouteInsightsPage from "./RouteInsightsPage";
import { useEffect, useRef, useState } from "react";
import { TransitRoute } from "../route.types";
import { fetchDeparturesEnrichRoutes } from "../route.utils";

const RouteInsightsPageContainer: React.FC = () => {
    const { routeSlug } = useParams<{ routeSlug: string }>();
    const navigate = useNavigate();
    const [enrichedRoute, setEnrichedRoute] = useState<TransitRoute>();
    const routeRef = useRef(enrichedRoute);
    const [currentTime, setCurrentTime] = useState(new Date());
    const timeRef = useRef(currentTime);
    const api = axios.create({
        // TODO: update to env var
        baseURL: 'http://localhost:7000'
    });
    const departureCount = 5;

    // keep routeRef updated
    useEffect(() => {
        routeRef.current = enrichedRoute;
    }, [enrichedRoute]);

    // keep timeRef updated
    useEffect(() => {
        timeRef.current = currentTime;
    }, [currentTime]);

    // fetch route initially
    useEffect(() => {
        const fetchAndEnrichRoute = async () => {
            try {
                const res = await fetchRouteBySlug(api, routeSlug || '', departureCount) as TransitRoute;
                setEnrichedRoute(res);
                routeRef.current = enrichedRoute;
            } catch (error) {
                console.error('Failed to fetch routes:', error);
            }
        };

        fetchAndEnrichRoute();
    }, []);

    // update route with up-to-date departures
    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentTime(new Date());

            const refreshRouteTimes = async () => {
                try {
                    if (!routeRef.current) {
                        return;
                    }
                    const routes = [routeRef.current];
                    const [res] = await fetchDeparturesEnrichRoutes(api, routes, departureCount) as TransitRoute[];
                    setEnrichedRoute(res);
                } catch (error) {
                    console.error('Failed to refresh departure times:', error);
                }
            }
                
            if (routeRef && routeRef.current && routeRef.current.departures && timeRef && timeRef.current) {
                const route = routeRef.current;
                const requiresRefresh =
                    [route.departures.fromOrigin, route.departures.fromDestination]
                    .some(departures => departures.some(dep_time => isPastTime(dep_time, timeRef.current)));

                if (requiresRefresh) {
                    refreshRouteTimes();
                }
            }
        }, 60000);

        return () => clearInterval(interval);
    }, []);

    const handleBack = () => {
        navigate('/');
    };

    return <RouteInsightsPage route={enrichedRoute} handleBack={handleBack} />;
}

export default RouteInsightsPageContainer;