import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import {Header} from './Header'
import { RouteList } from './RouteList'
import { RouteInsightsPage } from './RouteInsightsPage'

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
  return (
    <Router>
      <div className="min-h-screen bg-background">
        <Header />
        <Routes>
          <Route path="/" element={<RouteList />} />
          <Route path="/preview_page.html" element={<Navigate to="/" replace />} />
          <Route path="/:routeSlug/insights" element={<RouteInsightsPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  )
}