import { useState } from 'react'
import { RouteList } from './RouteList'
import { RouteInsights } from './RouteInsights'
import './styles/globals.css' // Ensure you have a styles.css file for global styles

export type TransitRoute = {
  id: string
  name: string
  type: 'ferry' | 'plane' | 'bus' | 'train'
  from: string
  to: string
  popularity: number
  status: 'on-time' | 'delayed' | 'cancelled'
  delay?: number
  departures: {
    fromOrigin: string[]      // departures from the "from" location
    fromDestination: string[] // departures from the "to" location
  }
  currentPosition?: {
    lat: number
    lng: number
    vehicle: string
  }
}

export default function App() {
  const [selectedRoute, setSelectedRoute] = useState<TransitRoute | null>(null)

  const handleRouteSelect = (route: TransitRoute) => {
    setSelectedRoute(route)
  }

  const handleBackToList = () => {
    setSelectedRoute(null)
  }

  return (
    <div className="min-h-screen bg-background">
      {selectedRoute ? (
        <RouteInsights route={selectedRoute} onBack={handleBackToList} />
      ) : (
        <RouteList onRouteSelect={handleRouteSelect} />
      )}
    </div>
  )
}