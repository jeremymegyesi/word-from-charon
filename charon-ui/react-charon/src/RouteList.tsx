import { useNavigate } from 'react-router-dom'
import { Card } from './react-charon-lib/card'
import { Badge } from './react-charon-lib/badge'
import { Clock, MapPin, TrendingUp, ArrowRight } from 'lucide-react'
import { TransitRoute } from './App'
import { mockRoutes, createRouteSlug } from './utils/routeUtils'

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

export function RouteList() {
  const navigate = useNavigate()

  const handleRouteSelect = (route: TransitRoute) => {
    const slug = createRouteSlug(route.name)
    navigate(`/${slug}/insights`)
  }

  const popularRoutes = mockRoutes
    .filter(route => route.popularity >= 75)
    .sort((a, b) => b.popularity - a.popularity)

  const allRoutes = [] as TransitRoute[]; // ajax call to fetch all routes

  const routesByType = allRoutes.reduce((acc, route) => {
    if (!acc[route.type]) acc[route.type] = []
    acc[route.type].push(route)
    return acc
  }, {} as Record<string, TransitRoute[]>)

  // Helper function to get the next departure time (from either direction)
  const getNextDeparture = (route: TransitRoute) => {
    const allDepartures = [...route.departures.fromOrigin, ...route.departures.fromDestination]
    return allDepartures[0] || 'No departures'
  }

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
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
          {popularRoutes.map((route) => (
            <Card 
              key={route.id} 
              className="p-4 cursor-pointer hover:shadow-md transition-shadow"
              onClick={() => handleRouteSelect(route)}
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
                    onClick={() => handleRouteSelect(route)}
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