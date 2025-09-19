import { Card, Badge } from '../../../react-charon-lib';
import { Clock, MapPin, TrendingUp, ArrowRight } from 'lucide-react';
import { TransitRoute } from '../route.types';
import { getStatusColor } from "../route.utils";
import { getTypeIcon } from "../route.utils";

interface RouteListProps {
  routes: Partial<TransitRoute>[];
  loading: boolean;
  handleRouteSelect: Function;
};

const renderRoutes = (routes: Partial<TransitRoute>[], handleRouteSelect: Function) => {
  return (
    
    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
      {routes.map((route) => (
        <Card 
          key={route.id} 
          className="p-4 cursor-pointer hover:shadow-md transition-shadow"
          onClick={() => handleRouteSelect(route)}
        >
          <div className="flex items-start justify-between mb-1">
            <div className="flex items-center gap-2">
              <span className="text-2xl">{getTypeIcon(route.type ?? 'bus')}</span>
              <div>
                <h3 className="font-medium">{route.displayName}</h3>
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
            <Badge className={`text-xs ${getStatusColor(route.status ?? 'on-time')} w-fit`}>
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
                  <span>{route.departures && route.departures.fromOrigin[0] || 'No departures'}</span>
                  <ArrowRight className="w-3 h-3 text-muted-foreground" />
                </div>
              </div>
              
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2 text-muted-foreground">
                  <Clock className="w-3 h-3" />
                  <span className="truncate">{route.to}</span>
                </div>
                <div className="flex items-center gap-1">
                  <span>{route.departures && route.departures.fromDestination[0] || 'No departures'}</span>
                  <ArrowRight className="w-3 h-3 text-muted-foreground" />
                </div>
              </div>
            </div>
          </div>
        </Card>
      ))}
    </div>
  );
}

const RouteList: React.FC<RouteListProps> = ({
  routes,
  loading,
  handleRouteSelect
}) => {

  return (
    <div className="container mx-auto px-4 py-6 max-w-6xl">
      <header className="mb-6">
        <h1 className="mb-2">Transit Monitor</h1>
        <p className="text-muted-foreground">Real-time transit information and insights</p>
      </header>

      {/* All routes */}
      <section className="mb-8">
        <div className="flex items-center gap-2 mb-4">
          <TrendingUp className="w-5 h-5 text-primary" />
          <h2>All Routes</h2>
        </div>
        {
          loading ?
            <div>Loading routes...</div> :
            (routes.length > 0 ? renderRoutes(routes, handleRouteSelect) : <p><i>No routes available.</i></p>)
            }
      </section>
    </div>
  )
};

export default RouteList;