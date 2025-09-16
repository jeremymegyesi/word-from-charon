
export type TransitRoute = {
  id: string;
  code: string;
  displayName: string;
  type: 'ferry' | 'plane' | 'bus' | 'train';
  from: string;
  to: string;
  popularity: number;
  status: 'on-time' | 'delayed' | 'cancelled';
  delay?: number;
  departures: {
    fromOrigin: string[] // departures from the "from" location
    fromDestination: string[] // departures from the "to" location
  };
  currentPosition?: {
    lat: number
    lng: number
    vehicle: string
  };
}
