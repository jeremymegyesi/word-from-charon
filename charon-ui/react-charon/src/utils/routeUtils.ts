import { TransitRoute } from '../charon-app/route.types'

export function createRouteSlug(routeName: string): string {
  return routeName
    .toLowerCase()
    .replace(/[^a-z0-9\s-]/g, '') // Remove special characters
    .replace(/\s+/g, '-') // Replace spaces with hyphens
    .replace(/-+/g, '-') // Replace multiple hyphens with single hyphen
    .trim()
}

export function findRouteBySlug(routes: TransitRoute[], slug: string): TransitRoute | undefined {
  return routes.find(route => createRouteSlug(route.displayName) === slug)
}

// Mock routes data - moved from RouteList component so it can be shared
export const mockRoutes: TransitRoute[] = [
  {
    id: '1',
    displayName: 'Seattle - Vancouver Express',
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
]