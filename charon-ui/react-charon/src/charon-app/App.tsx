import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import {Header} from './Header'
import RouteList from './route-list/RouteList.container'
import { RouteInsightsPage } from './RouteInsightsPage'

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