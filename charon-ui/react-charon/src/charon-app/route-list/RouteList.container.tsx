import { useEffect, useState } from 'react';
import RouteList from './RouteList';
import axios from 'axios';
import { TransitRoute } from '../route.types';
import { convertTransitRoute, getMatchingScheduleKey, to12HourFormat } from './RouteList.utils';
import { TransitRouteResponse } from "./RouteList.types";
import { createRouteSlug } from '../../utils/routeUtils';
import { useNavigate } from 'react-router-dom';

const RouteListContainer: React.FC = () => {
    const [enrichedRoutes, setEnrichedRoutes] = useState<TransitRoute[]>([]);
    const [loading, setLoading] = useState(true);

    const api = axios.create({
        // TODO: update to env var
        baseURL: 'http://localhost:7000'
    });

    // fetch route data and enrich with schedule info
    useEffect(() => {
        const fetchAndEnrichRoutes = async () => {
            setLoading(true);
            try {
                const res = await api.get('/routes');
                const baseRoutes = (res.data as TransitRouteResponse[]).map(convertTransitRoute) as TransitRoute[];

                const enriched = await Promise.all(
                    baseRoutes.map(async route => {
                        try {
                            const scheduleRes = await api.get(`/schedule/${route.code}/next`);
                            const toKey = getMatchingScheduleKey(route.to, Object.keys(scheduleRes.data));
                            const fromKey = getMatchingScheduleKey(route.from, Object.keys(scheduleRes.data));

                            const fromTimes = fromKey ? scheduleRes.data[`${fromKey}`] as string[] : [];
                            const toTimes = toKey ? scheduleRes.data[`${toKey}`] as string[] : [];
                            
                            return {
                                ...route,
                                departures: {
                                    fromOrigin: fromTimes.map(to12HourFormat),
                                    fromDestination: toTimes.map(to12HourFormat)
                                }
                            };
                        } catch (err) {
                            return {
                                ...route,
                                departures: {
                                    fromOrigin: [],
                                    fromDestination: []
                                }
                            };
                        }
                    })
                );
                setEnrichedRoutes(enriched);
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
        const slug = createRouteSlug(route.code);
        navigate(`/${slug}/insights`);
    };

    return (
        <RouteList loading={loading} routes={enrichedRoutes} handleRouteSelect={handleRouteSelect} />
    );
};

export default RouteListContainer;