import { TransitRoute } from "../route.types";
import { TransitRouteResponse, TransitRouteType } from "./RouteList.types";

export const to12HourFormat = (time: string): string => {
  if (!time.match(/^\d{1,2}:\d{2}:\d{2}$/)) {
    return time; // return original if format is unexpected
  }
  const [hourStr, minuteStr] = time.split(':');
  let hour = parseInt(hourStr, 10);
  const minute = minuteStr;
  const ampm = hour >= 12 ? 'pm' : 'am';
  hour = hour % 12 || 12;
  return `${hour}:${minute} ${ampm}`;
};

export const convertTransitRoute = (routeResponse: TransitRouteResponse): Partial<TransitRoute> => {
  const data = {
    id: routeResponse.id,
    code: routeResponse.code,
    displayName: routeResponse.displayName,
    type: (routeResponse.routeType as TransitRouteType).type.toString().toLowerCase() as TransitRoute['type'],
    // mock data for other fields
    from: routeResponse.fromLocation,
    to: routeResponse.toLocation,
    popularity: 51,
    status: 'on-time' as TransitRoute['status'],
    delay: 0,
    currentPosition: {
      lat: 49.1659,
      lng: -123.9401,
      vehicle: 'ISLAND KWIGWIS'
    },
  };

  return data;
};

export const getMatchingScheduleKey = (shortName: string, scrapedNames: string[]): string | null => {
  scrapedNames = scrapedNames.filter(key => key.toLowerCase().includes(shortName.toLowerCase()));
  let matchingKey = null;
  if (scrapedNames.length > 0) {
      matchingKey = scrapedNames.reduce((prev, curr) => {
          const prevIndex = prev.toLowerCase().indexOf(shortName.toLowerCase());
          const currIndex = curr.toLowerCase().indexOf(shortName.toLowerCase());
          return (currIndex !== -1 && (prevIndex === -1 || currIndex < prevIndex)) ? curr : prev;
      });
  }
  return matchingKey;
}

export const getTypeIcon = (type: TransitRoute['type']) => {
  const icons = {
    ferry: '⛴️',
    plane: '✈️',
    bus: '🚌',
    train: '🚆'
  }
  return icons[type]
}
;
export const getStatusColor = (status: TransitRoute['status']) => {
  switch (status) {
    case 'on-time': return 'bg-green-100 text-green-800'
    case 'delayed': return 'bg-yellow-100 text-yellow-800'
    case 'cancelled': return 'bg-red-100 text-red-800'
  }
}

