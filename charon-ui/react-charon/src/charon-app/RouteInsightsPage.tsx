import { useParams, useNavigate } from 'react-router-dom'
import { RouteInsights } from './RouteInsights'
import { findRouteBySlug, mockRoutes } from '../utils/routeUtils'

export function RouteInsightsPage() {
  const { routeSlug } = useParams<{ routeSlug: string }>()
  const navigate = useNavigate()

  const route = routeSlug ? findRouteBySlug(mockRoutes, routeSlug) : null

  const handleBack = () => {
    navigate('/')
  }

  if (!route) {
    return (
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        <div className="text-center">
          <h1 className="mb-4">Route Not Found</h1>
          <p className="text-muted-foreground mb-6">The route you're looking for doesn't exist.</p>
          <button 
            onClick={handleBack}
            className="bg-primary text-primary-foreground px-4 py-2 rounded hover:opacity-80"
          >
            Back to Routes
          </button>
        </div>
      </div>
    )
  }

  return <RouteInsights route={route} onBack={handleBack} />
}