import { useNavigate, useParams } from "react-router-dom";
import { fetchRouteBySlug } from './RouteInsightsPage.utils';
import axios from "axios";
import RouteInsightsPage from "./RouteInsightsPage";
import { useEffect, useState } from "react";
import { TransitRoute } from "../route.types";

const RouteInsightsPageContainer: React.FC = () => {
    const { routeSlug } = useParams<{ routeSlug: string }>();
    const navigate = useNavigate();
    const [enrichedRoute, setEnrichedRoute] = useState<TransitRoute>();
    const api = axios.create({
        // TODO: update to env var
        baseURL: 'http://localhost:7000'
    });

    useEffect(() => {
        const fetchAndEnrichRoute = async () => {
            try {
                const res = await fetchRouteBySlug(api, routeSlug || '') as TransitRoute;
                setEnrichedRoute(res);
            } catch (error) {
                console.error('Failed to fetch routes:', error);
            }
        };
        fetchAndEnrichRoute();
    }, []);

    const handleBack = () => {
        navigate('/');
    };

    return <RouteInsightsPage route={enrichedRoute} handleBack={handleBack} />;
}

export default RouteInsightsPageContainer;