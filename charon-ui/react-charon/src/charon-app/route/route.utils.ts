import { TransitRouteResponse, TransitRouteType } from './route-list/RouteList.types';
import { TransitRoute } from './route.types'
import { AxiosInstance } from 'axios';

export const getTypeIcon = (type: TransitRoute['type']) => {
  const icons = {
    ferry: '⛴️',
    plane: '✈️',
    bus: '🚌',
    train: '🚆'
  };
  return icons[type];
};

export const getStatusColor = (status: TransitRoute['status']) => {
  switch (status) {
    case 'on-time': return 'bg-green-100 text-green-800';
    case 'delayed': return 'bg-yellow-100 text-yellow-800';
    case 'cancelled': return 'bg-red-100 text-red-800';
  }
};

/*** Map API response to TransitRoute type ***/
export const convertTransitRoute = (routeResponse: TransitRouteResponse): Partial<TransitRoute> => {
  const data = {
    id: routeResponse.id,
    code: routeResponse.code,
    displayName: routeResponse.displayName,
    type: (routeResponse.routeType as TransitRouteType).type.toString().toLowerCase() as TransitRoute['type'],
    from: routeResponse.fromLocation,
    to: routeResponse.toLocation,
    // mock data for other fields
    popularity: 51,
    status: 'on-time' as TransitRoute['status'],
    delay: 0,
    currentPosition: {
      lat: 49.1659,
      lng: -123.9401,
      vehicle: 'ISLAND KWIGWIS & ISLAND GWAWIS'
    },
  };

  return data;
};

/*** Transit terminal keys are scraped from website so will not match exactly. Find the best match ***/
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
};

/*** Convert 24-hour time (HH:mm:ss) to 12-hour format with AM/PM ***/
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

/*** Fetch routes and enrich with upcoming departures ***/
export async function fetchRoutesEnrichWithDepartures(api: AxiosInstance, routeCode?: string, departureCount?: number): Promise<Partial<TransitRoute[]>> {
  const routeUrl = '/routes' + (routeCode ? `/${routeCode}`: '');
  let res = await api.get(routeUrl);
  const baseRoutes = 
    (!Array.isArray(res.data) ? [res.data] : res.data as TransitRouteResponse[])
    .map(convertTransitRoute) as TransitRoute[];

    const enriched = fetchDeparturesEnrichRoutes(api, baseRoutes, departureCount);

  return enriched;
}

export async function fetchDeparturesEnrichRoutes(api: AxiosInstance, baseRoutes: TransitRoute[], departureCount?: number): Promise<Partial<TransitRoute[]>> {
  const departureUrlParam = departureCount ? `?count=${departureCount}` : '';

  const enriched = await Promise.all(
      baseRoutes.map(async route => {
          try {
              const scheduleRes = await api.get(`/schedule/${route.code}/next` + departureUrlParam);
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

  return enriched;
}

/*** Sanitize route code to create URL-friendly slug ***/
export function sanitizeRouteSlug(slug: string): string {
  return slug
    .replace(/[^a-zA-Z0-9\s-]/g, '') // Remove special characters
    .replace(/\s+/g, '-') // Replace spaces with hyphens
    .replace(/-+/g, '-') // Replace multiple hyphens with single hyphen
    .trim()
}

// Mock routes data - moved from RouteList component so it can be shared
export const mockRoutes: TransitRoute[] = [
  {
    id: '1',
    displayName: 'Seattle - Vancouver Express',
    code: 'SVX',
    type: 'ferry',
    from: 'Seattle',
    to: 'Vancouver',
    popularity: 95,
    status: 'on-time',
    departures: {
      fromOrigin: ['8:30 AM', '11:15 AM', '2:45 PM', '6:30 PM'],
      fromDestination: ['7:00 AM', '10:30 AM', '1:15 PM', '5:00 PM', '8:45 PM']
    },
    currentPosition: { lat: 47.6062, lng: -122.3321, vehicle: 'MV Spirit' }
  },
  {
    id: '2',
    displayName: 'LAX - SFO Shuttle',
    code: 'LAX-SFO',
    type: 'plane',
    from: 'Los Angeles',
    to: 'San Francisco',
    popularity: 88,
    status: 'delayed',
    delay: 15,
    departures: {
      fromOrigin: ['9:00 AM', '10:30 AM', '12:00 PM', '1:30 PM', '3:00 PM'],
      fromDestination: ['8:15 AM', '9:45 AM', '11:15 AM', '12:45 PM', '2:15 PM']
    },
    currentPosition: { lat: 34.0522, lng: -118.2437, vehicle: 'AA1234' }
  },
  {
    id: '3',
    displayName: 'Downtown Circulator',
    code: 'DTC',
    type: 'bus',
    from: 'Union Station',
    to: 'Financial District',
    popularity: 82,
    status: 'on-time',
    departures: {
      fromOrigin: ['8:15 AM', '8:30 AM', '8:45 AM', '9:00 AM', '9:15 AM'],
      fromDestination: ['8:05 AM', '8:20 AM', '8:35 AM', '8:50 AM', '9:05 AM']
    },
    currentPosition: { lat: 34.0560, lng: -118.2368, vehicle: 'Bus #412' }
  },
  {
    id: '4',
    displayName: 'Coast Starlight',
    code: 'CSL',
    type: 'train',
    from: 'Los Angeles',
    to: 'Seattle',
    popularity: 76,
    status: 'on-time',
    departures: {
      fromOrigin: ['6:00 AM'],
      fromDestination: ['6:00 PM']
    },
    currentPosition: { lat: 36.7783, lng: -119.4179, vehicle: 'Train #14' }
  },
  {
    id: '5',
    displayName: 'Golden Gate Ferry',
    code: 'GGF',
    type: 'ferry',
    from: 'San Francisco',
    to: 'Sausalito',
    popularity: 71,
    status: 'on-time',
    departures: {
      fromOrigin: ['8:45 AM', '10:15 AM', '12:30 PM', '3:00 PM', '5:30 PM'],
      fromDestination: ['9:30 AM', '11:00 AM', '1:15 PM', '3:45 PM', '6:15 PM']
    },
    currentPosition: { lat: 37.7749, lng: -122.4194, vehicle: 'Ferry Sausalito' }
  },
  {
    id: '6',
    displayName: 'Regional Express',
    code: 'REX',
    type: 'bus',
    from: 'Sacramento',
    to: 'San Francisco',
    popularity: 64,
    status: 'delayed',
    delay: 8,
    departures: {
      fromOrigin: ['7:30 AM', '9:45 AM', '12:15 PM', '4:30 PM'],
      fromDestination: ['6:15 AM', '8:30 AM', '11:00 AM', '3:15 PM', '7:00 PM']
    },
    currentPosition: { lat: 38.5816, lng: -121.4944, vehicle: 'Express #789' }
  }
];
