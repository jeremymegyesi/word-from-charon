import { VehicleMapConfig } from '../VehicleMap.types';

interface VehicleMapProps {
  mapConfig: VehicleMapConfig['config'];
  src: string;
};

const VehicleMap = ({ mapConfig, src }: VehicleMapProps) => {

  return (
    <iframe
      src={src}
      width={mapConfig.width}
      height={mapConfig.height}
      style={{ border: 'none' }}
      title="Vessel Map"
    />
  )
}

export default VehicleMap;