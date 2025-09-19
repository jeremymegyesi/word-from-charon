import { VehicleMap } from './VehicleMap';
import { TransitRoute } from '../route/route.types';
import { VehicleMapConfig } from './VehicleMap.types';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { TransitRouteResponse } from '../route/route-list/RouteList.types';

interface VehicleMapContainerProps {
    route: TransitRoute;
};

interface MapConfigResponse {
    id: string;
    route: TransitRouteResponse;
    mapType: string;
    config: string;
};

const mapMapConfigResponse = (res: MapConfigResponse): VehicleMapConfig => {
    return {
        mapType: res.mapType as VehicleMapConfig['mapType'],
        config: JSON.parse(res.config) as VehicleMapConfig['config'],
    };
}

const VehicleMapContainer: React.FC<VehicleMapContainerProps> = ({route}) => {
    const [mapConfig, setMapConfig] = useState<VehicleMapConfig>();
    useEffect(() => {
        const api = axios.create({ 
            // TODO: update to env var
            baseURL: 'http://localhost:7000'
        });
        const fetchMapConfig = async () => {
            try {
                const res = await api.get<MapConfigResponse>(`/map/${route.code}`);
                setMapConfig(mapMapConfigResponse(res.data));
            } catch (error) {
                console.error('Failed to fetch map config:', error);
            }
        };
        fetchMapConfig();
    }, []);

    return (
        mapConfig ? <VehicleMap mapConfig={mapConfig} /> : null
    )
};

export default VehicleMapContainer;