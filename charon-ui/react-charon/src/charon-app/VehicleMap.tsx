import { MapPin } from 'lucide-react'
import { TransitRoute } from './route.types'

interface VehicleMapProps {
  position: {
    lat: number
    lng: number
    vehicle: string
  }
  route: TransitRoute
}

export function VehicleMap({ position, route }: VehicleMapProps) {
  // Mock map implementation - in a real app, this would use a mapping service
  // like Google Maps, Mapbox, or Leaflet
  
  const mapStyle = {
    backgroundImage: `linear-gradient(45deg, 
      hsl(var(--muted)) 25%, 
      transparent 25%, 
      transparent 75%, 
      hsl(var(--muted)) 75%, 
      hsl(var(--muted))),
      linear-gradient(45deg, 
      hsl(var(--muted)) 25%, 
      transparent 25%, 
      transparent 75%, 
      hsl(var(--muted)) 75%, 
      hsl(var(--muted)))`,
    backgroundSize: '20px 20px',
    backgroundPosition: '0 0, 10px 10px'
  }

  return (
    <div className="relative">
      <div 
        className="h-96 rounded-lg border border-border relative overflow-hidden"
        style={mapStyle}
      >
        {/* Mock map with vehicle position */}
        <div className="absolute inset-0 bg-gradient-to-br from-blue-50/50 to-green-50/50">
          
          {/* Route line */}
          <div 
            className="absolute top-1/2 left-4 right-4 h-1 bg-primary/30 rounded-full transform -translate-y-1/2"
            style={{
              background: 'linear-gradient(90deg, hsl(var(--primary)) 0%, hsl(var(--primary))/50 50%, hsl(var(--primary)) 100%)'
            }}
          />
          
          {/* Start location */}
          <div className="absolute top-1/2 left-4 transform -translate-y-1/2 -translate-x-1/2">
            <div className="w-4 h-4 bg-green-500 rounded-full border-2 border-white shadow-lg" />
            <div className="absolute -bottom-6 left-1/2 transform -translate-x-1/2 whitespace-nowrap">
              <div className="text-xs bg-white px-2 py-1 rounded shadow border">
                {route.from}
              </div>
            </div>
          </div>
          
          {/* End location */}
          <div className="absolute top-1/2 right-4 transform -translate-y-1/2 translate-x-1/2">
            <div className="w-4 h-4 bg-red-500 rounded-full border-2 border-white shadow-lg" />
            <div className="absolute -bottom-6 left-1/2 transform -translate-x-1/2 whitespace-nowrap">
              <div className="text-xs bg-white px-2 py-1 rounded shadow border">
                {route.to}
              </div>
            </div>
          </div>
          
          {/* Vehicle position (roughly 60% of the way) */}
          <div 
            className="absolute top-1/2 transform -translate-y-1/2 -translate-x-1/2 z-10"
            style={{ left: '60%' }}
          >
            <div className="relative">
              {/* Vehicle icon */}
              <div className="w-6 h-6 bg-primary rounded-full border-2 border-white shadow-lg flex items-center justify-center">
                <span className="text-white text-xs">🚍</span>
              </div>
              
              {/* Pulsing animation */}
              <div className="absolute inset-0 w-6 h-6 bg-primary rounded-full animate-ping opacity-20" />
              
              {/* Vehicle info popup */}
              <div className="absolute -top-10 left-1/2 transform -translate-x-1/2 whitespace-nowrap">
                <div className="bg-primary text-primary-foreground px-3 py-2 rounded shadow-lg text-xs">
                  {position.vehicle}
                  <div className="absolute bottom-0 left-1/2 transform translate-y-1/2 -translate-x-1/2 rotate-45 w-2 h-2 bg-primary" />
                </div>
              </div>
            </div>
          </div>
          
          {/* Coordinates display */}
          <div className="absolute bottom-4 left-4">
            <div className="bg-white/90 px-3 py-2 rounded shadow text-xs">
              <div className="flex items-center gap-1">
                <MapPin className="w-3 h-3" />
                <span>Lat: {position.lat.toFixed(4)}, Lng: {position.lng.toFixed(4)}</span>
              </div>
            </div>
          </div>
          
          {/* Legend */}
          <div className="absolute top-4 right-4">
            <div className="bg-white/90 px-3 py-2 rounded shadow text-xs space-y-1">
              <div className="flex items-center gap-2">
                <div className="w-3 h-3 bg-green-500 rounded-full" />
                <span>Origin</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-3 h-3 bg-primary rounded-full" />
                <span>Vehicle</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-3 h-3 bg-red-500 rounded-full" />
                <span>Destination</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      {/* Map instructions */}
      <div className="mt-3 text-sm text-muted-foreground text-center">
        Live vehicle tracking • Updated every 30 seconds
      </div>
    </div>
  )
}