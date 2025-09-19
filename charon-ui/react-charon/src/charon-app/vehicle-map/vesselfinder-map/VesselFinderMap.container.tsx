import VesselFinderMap from './VesselFinderMap';
import { VehicleMapConfig } from '../VehicleMap.types';

interface VesselFinderContainerProps {
    config: VehicleMapConfig["config"];
}

const VesselFinderMapContainer: React.FC<VesselFinderContainerProps> = ({config}) => {
    let mapSrc = 'https://www.vesselfinder.com/aismap?';
    if (config) {
        mapSrc +=
            `zoom=${config.zoom}&amp;` +
            `lat=${config.latitude}&amp;` +
            `lon=${config.longitude}&amp;` +
            `height=${config.height}&amp;` +
            `names=${config.names}&amp;` +
            `fleet=${config.fleet}&amp;`;
    }

    return (
        <VesselFinderMap mapConfig={config} src={mapSrc} />
    )
};

export default VesselFinderMapContainer;