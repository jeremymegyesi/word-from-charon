import { VehicleMapConfig } from './VehicleMap.types';
import VesselFinderMap from './vesselfinder-map/VesselFinderMap.container';

interface VehicleMapProps {
  mapConfig: VehicleMapConfig;
};

const renderVesselFinderMap = (mapConfig: VehicleMapConfig) => {
  return <VesselFinderMap config={mapConfig.config} />;
}

export function VehicleMap({ mapConfig }: VehicleMapProps) {
  const mapType = mapConfig?.mapType || 'none';
  return (
    <>
      {
        (mapType === 'vesselfinder' && renderVesselFinderMap(mapConfig)) ||
        mapType === 'none' && <div>No map available for this vehicle type.</div>
      }
    </>
  )
}