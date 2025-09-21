import VesselFinderMap from './VesselFinderMap';
import { VehicleMapConfig } from '../VehicleMap.types';
import { useEffect, useState } from 'react';

interface VesselFinderContainerProps {
    config: VehicleMapConfig["config"];
}

const VesselFinderMapContainer: React.FC<VesselFinderContainerProps> = ({config}) => {
    let mapSrc = 'https://www.vesselfinder.com/aismap';
    if (config) {
        mapSrc +=
            `?zoom=${config.zoom}&` +
            `lat=${config.latitude}&` +
            `lon=${config.longitude}&` +
            `names=${config.names}&` +
            `fleet=${config.fleet}&` +
            `fleet_name=gabnah`
    }

    const [key, setKey] = useState(Date.now());

    // Update key to reload map every minute
    useEffect(() => {
        const interval = setInterval(() => {
            setKey(Date.now());
        }, 60000);

        return () => clearInterval(interval);
    }, []);


    return (
        <VesselFinderMap mapConfig={config} src={`${mapSrc}&ts=${key}`} />
    )
};

export default VesselFinderMapContainer;