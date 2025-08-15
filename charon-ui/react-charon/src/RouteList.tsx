import { Card } from './react-charon-lib/card'
import { Badge } from './react-charon-lib/badge'
import { Clock, MapPin, TrendingUp, ArrowRight } from 'lucide-react'
import { TransitRoute } from './App'

const mockRoutes: TransitRoute[] = [
  {
    id: '1',
    name: 'Seattle - Vancouver Express',
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
    name: 'LAX - SFO Shuttle',
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
    name: 'Downtown Circulator',
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
    name: 'Coast Starlight',
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
    name: 'Golden Gate Ferry',
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
    name: 'Regional Express',
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

interface RouteListProps {
  onRouteSelect: (route: TransitRoute) => void
}

const getTypeIcon = (type: TransitRoute['type']) => {
  const icons = {
    ferry: '⛴️',
    plane: '✈️',
    bus: '🚌',
    train: '🚆'
  }
  return icons[type]
}

const getStatusColor = (status: TransitRoute['status']) => {
  switch (status) {
    case 'on-time': return 'bg-green-100 text-green-800'
    case 'delayed': return 'bg-yellow-100 text-yellow-800'
    case 'cancelled': return 'bg-red-100 text-red-800'
  }
}

export function RouteList({ onRouteSelect }: RouteListProps) {
  const popularRoutes = mockRoutes
    .filter(route => route.popularity >= 75)
    .sort((a, b) => b.popularity - a.popularity)

  const routesByType = mockRoutes.reduce((acc, route) => {
    if (!acc[route.type]) acc[route.type] = []
    acc[route.type].push(route)
    return acc
  }, {} as Record<string, TransitRoute[]>)

  return (
    <div className="container mx-auto px-4 py-6 max-w-6xl">
      <header className="mb-6">
        <h1 className="mb-2">Transit Monitor</h1>
        <p className="text-muted-foreground">Real-time transit information and insights</p>
      </header>

      {/* Popular Routes */}
      <section className="mb-8">
        <div className="flex items-center gap-2 mb-4">
          <TrendingUp className="w-5 h-5 text-primary" />
          <h2>Popular Routes</h2>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {popularRoutes.map((route) => (
            <Card 
              key={route.id} 
              className="p-4 cursor-pointer hover:shadow-md transition-shadow"
              onClick={() => onRouteSelect(route)}
            >
              <div className="flex items-start justify-between mb-1">
                <div className="flex items-center gap-2">
                  <span className="text-2xl">{getTypeIcon(route.type)}</span>
                  <div>
                    <h3 className="font-medium">{route.name}</h3>
                    <div className="flex items-center gap-1 text-muted-foreground text-sm">
                      <MapPin className="w-3 h-3" />
                      <span>{route.from} ↔ {route.to}</span>
                    </div>
                  </div>
                </div>
                <Badge className="text-xs">
                  {route.popularity}% popular
                </Badge>
              </div>
              
              <div className="space-y-2">
                <Badge className={`text-xs ${getStatusColor(route.status)} w-fit`}>
                  {route.status === 'delayed' && route.delay ? 
                    `${route.delay}min delay` : route.status
                  }
                </Badge>
                
                {/* Next departures from both terminals */}
                <div className="space-y-2 text-sm">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Clock className="w-3 h-3" />
                      <span className="truncate">{route.from}</span>
                    </div>
                    <div className="flex items-center gap-1">
                      <span>{route.departures.fromOrigin[0] || 'No departures'}</span>
                      <ArrowRight className="w-3 h-3 text-muted-foreground" />
                    </div>
                  </div>
                  
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Clock className="w-3 h-3" />
                      <span className="truncate">{route.to}</span>
                    </div>
                    <div className="flex items-center gap-1">
                      <span>{route.departures.fromDestination[0] || 'No departures'}</span>
                      <ArrowRight className="w-3 h-3 text-muted-foreground" />
                    </div>
                  </div>
                </div>
              </div>
            </Card>
          ))}
        </div>
      </section>

      {/* Routes by Category */}
      <section>
        <h2 className="mb-4">All Routes by Category</h2>
        <div className="space-y-6">
          {Object.entries(routesByType).map(([type, routes]) => (
            <div key={type}>
              <h3 className="mb-3 flex items-center gap-2">
                <span className="text-xl">{getTypeIcon(type as TransitRoute['type'])}</span>
                <span className="capitalize">{type} Routes</span>
                <Badge variant="secondary" className="ml-2 text-xs">
                  {routes.length}
                </Badge>
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                {routes.map((route) => (
                  <Card 
                    key={route.id} 
                    className="p-3 cursor-pointer hover:shadow-md transition-shadow"
                    onClick={() => onRouteSelect(route)}
                  >
                    <h4 className="mb-2">{route.name}</h4>
                    <div className="flex items-center gap-1 text-muted-foreground text-sm mb-1">
                      <MapPin className="w-3 h-3" />
                      <span>{route.from} ↔ {route.to}</span>
                    </div>
                    
                    <div className="space-y-2">
                      <Badge className={`text-xs ${getStatusColor(route.status)} w-fit`}>
                        {route.status === 'delayed' && route.delay ? 
                          `${route.delay}min delay` : route.status
                        }
                      </Badge>
                      
                      {/* Next departures from both terminals */}
                      <div className="space-y-2 text-sm">
                        <div className="flex items-center justify-between">
                          <div className="flex items-center gap-2 text-muted-foreground">
                            <Clock className="w-3 h-3" />
                            <span className="truncate">{route.from}</span>
                          </div>
                          <div className="flex items-center gap-1">
                            <span>{route.departures.fromOrigin[0] || 'No departures'}</span>
                            <ArrowRight className="w-3 h-3 text-muted-foreground" />
                          </div>
                        </div>
                        
                        <div className="flex items-center justify-between">
                          <div className="flex items-center gap-2 text-muted-foreground">
                            <Clock className="w-3 h-3" />
                            <span className="truncate">{route.to}</span>
                          </div>
                          <div className="flex items-center gap-1">
                            <span>{route.departures.fromDestination[0] || 'No departures'}</span>
                            <ArrowRight className="w-3 h-3 text-muted-foreground" />
                          </div>
                        </div>
                      </div>
                    </div>
                  </Card>
                ))}
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  )
}