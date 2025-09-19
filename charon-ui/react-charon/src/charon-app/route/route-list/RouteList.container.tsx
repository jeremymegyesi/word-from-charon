import { useEffect, useState } from 'react';
import RouteList from './RouteList';
import axios from 'axios';
import { TransitRoute } from '../route.types';
import { fetchRoutesEnrichWithDepartures } from "../route.utils";
import { sanitizeRouteSlug } from '../route.utils';
import { useNavigate } from 'react-router-dom';

const RouteListContainer: React.FC = () => {
    const [enrichedRoutes, setEnrichedRoutes] = useState<Partial<TransitRoute>[]>([]);
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
                const res = await fetchRoutesEnrichWithDepartures(api) as Partial<TransitRoute>[];
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

    return (
        <RouteList loading={loading} routes={enrichedRoutes} handleRouteSelect={handleRouteSelect} />
    );
};

export default RouteListContainer;