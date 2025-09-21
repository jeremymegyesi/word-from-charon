import { ArrowLeft, Clock, MapPin, Calendar, AlertTriangle, CheckCircle, XCircle, ArrowRight } from 'lucide-react';
import { Card, Button, Badge, Separator } from '../../../react-charon-lib';
import { TransitRoute } from '../route.types'
import VehicleMap from '../../vehicle-map/VehicleMap.container'
import { getTypeIcon, getStatusColor } from '../route.utils'

interface RouteInsightsProps {
  route: TransitRoute
  onBack: () => void
}

const getStatusIcon = (status: TransitRoute['status']) => {
  switch (status) {
    case 'on-time': return <CheckCircle className="w-4 h-4 text-green-600" />
    case 'delayed': return <AlertTriangle className="w-4 h-4 text-yellow-600" />
    case 'cancelled': return <XCircle className="w-4 h-4 text-red-600" />
  }
}

export function RouteInsights({ route, onBack }: RouteInsightsProps) {
  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="bg-card border-b border-border">
        <div className="container mx-auto px-4 py-4 max-w-6xl">
          <div className="flex items-center gap-4">
            <Button variant="ghost" size="sm" onClick={onBack}>
              <ArrowLeft className="w-4 h-4 mr-2" />
              Back to Routes
            </Button>
            <Separator orientation="vertical" className="h-6" />
            <div className="flex items-center gap-3">
              <span className="text-2xl">{getTypeIcon(route.type)}</span>
              <div>
                <h1>{route.displayName}</h1>
                <div className="flex items-center gap-2 text-muted-foreground text-sm">
                  <MapPin className="w-3 h-3" />
                  <span>{route.from} ↔ {route.to}</span>
                </div>
              </div>
            </div>
            <div className="ml-auto flex items-center gap-2">
              {getStatusIcon(route.status)}
              <Badge className={`${getStatusColor(route.status)}`}>
                {route.status === 'delayed' && route.delay ? 
                  `Delayed ${route.delay} minutes` : 
                  route.status.replace('-', ' ')
                }
              </Badge>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        <div className="grid lg:grid-cols-3 gap-6">
          {/* Left Column - Next Departures & Status */}
          <div className="lg:col-span-1 space-y-6">
            {/* Departures from Origin */}
            <Card className="p-6">
              <div className="flex items-center gap-2 mb-4">
                <Clock className="w-5 h-5 text-primary" />
                <h2>{route.from} Departures</h2>
              </div>
              <div className="space-y-3">
                {route.departures.fromOrigin.map((time, index) => (
                  <div key={`origin-${time}`} className="flex items-center justify-between p-3 bg-muted rounded-lg">
                    <div className="flex items-center gap-2">
                      <span>{time}</span>
                      <ArrowRight className="w-3 h-3 text-muted-foreground" />
                      <span className="text-sm text-muted-foreground">{route.to}</span>
                    </div>
                    <div className="flex items-center gap-2">
                      {index === 0 && route.status === 'delayed' && (
                        <Badge variant="secondary" className="text-xs bg-yellow-100 text-yellow-800">
                          +{route.delay}min
                        </Badge>
                      )}
                      {index === 0 && (
                        <Badge variant="secondary" className="text-xs">Next</Badge>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </Card>

            {/* Departures from Destination */}
            <Card className="p-6">
              <div className="flex items-center gap-2 mb-4">
                <Clock className="w-5 h-5 text-primary" />
                <h2>{route.to} Departures</h2>
              </div>
              <div className="space-y-3">
                {route.departures.fromDestination.map((time, index) => (
                  <div key={`destination-${time}`} className="flex items-center justify-between p-3 bg-muted rounded-lg">
                    <div className="flex items-center gap-2">
                      <span>{time}</span>
                      <ArrowRight className="w-3 h-3 text-muted-foreground" />
                      <span className="text-sm text-muted-foreground">{route.from}</span>
                    </div>
                    <div className="flex items-center gap-2">
                      {index === 0 && route.status === 'delayed' && (
                        <Badge variant="secondary" className="text-xs bg-yellow-100 text-yellow-800">
                          +{route.delay}min
                        </Badge>
                      )}
                      {index === 0 && (
                        <Badge variant="secondary" className="text-xs">Next</Badge>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </Card>

            {/* Full Schedule Button */}
            <div className="pt-4">
              <Button variant="outline" className="w-full">
                <Calendar className="w-4 h-4 mr-2" />
                View Full Schedule
              </Button>
            </div>

            {/* Route Statistics */}
            <Card className="p-6">
              <h2 className="mb-4">Route Statistics</h2>
              <div className="space-y-4">
                <div className="flex justify-between items-center">
                  <span className="text-muted-foreground">Popularity Rank</span>
                  <span>#{Math.floor((100 - route.popularity) / 10) + 1}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-muted-foreground">On-Time Rate</span>
                  <span>{route.popularity}%</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-muted-foreground">Avg. Delay</span>
                  <span>{Math.floor(Math.random() * 5 + 1)} min</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-muted-foreground">Daily Trips</span>
                  <span>{route.departures.fromOrigin.length + route.departures.fromDestination.length}</span>
                </div>
              </div>
            </Card>
          </div>

          {/* Right Column - Map & Current Position */}
          <div className="lg:col-span-2">
            <Card className="p-6">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-2">
                  <MapPin className="w-5 h-5 text-primary" />
                  <h2>Current Vehicle Position</h2>
                </div>
                {route.currentPosition && (
                  <Badge variant="secondary" className="text-xs">
                    {route.currentPosition.vehicle}
                  </Badge>
                )}
              </div>
              
              {route.currentPosition ? (
                <VehicleMap
                  route={route}
                />
              ) : (
                <div className="h-96 bg-muted rounded-lg flex items-center justify-center">
                  <div className="text-center text-muted-foreground">
                    <MapPin className="w-12 h-12 mx-auto mb-2 opacity-50" />
                    <p>Vehicle position not available</p>
                  </div>
                </div>
              )}
            </Card>

            {/* Additional Insights */}
            <Card className="p-6 mt-6">
              <h2 className="mb-4">Recent Activity</h2>
              <div className="space-y-3">
                <div className="flex items-start gap-3 p-3 bg-muted rounded-lg">
                  <CheckCircle className="w-4 h-4 text-green-600 mt-0.5 flex-shrink-0" />
                  <div className="text-sm">
                    <p><strong>On-time departure</strong> from {route.from} at {route.departures.fromOrigin[1] || route.departures.fromOrigin[0]}</p>
                    <p className="text-muted-foreground">2 hours ago</p>
                  </div>
                </div>
                
                {route.status === 'delayed' && (
                  <div className="flex items-start gap-3 p-3 bg-muted rounded-lg">
                    <AlertTriangle className="w-4 h-4 text-yellow-600 mt-0.5 flex-shrink-0" />
                    <div className="text-sm">
                      <p><strong>Delay reported</strong> - {route.delay} minutes behind schedule</p>
                      <p className="text-muted-foreground">15 minutes ago</p>
                    </div>
                  </div>
                )}
                
                <div className="flex items-start gap-3 p-3 bg-muted rounded-lg">
                  <CheckCircle className="w-4 h-4 text-green-600 mt-0.5 flex-shrink-0" />
                  <div className="text-sm">
                    <p><strong>Route completed</strong> - Arrived at {route.to}</p>
                    <p className="text-muted-foreground">4 hours ago</p>
                  </div>
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}