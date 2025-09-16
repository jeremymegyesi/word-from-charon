export interface TransitRouteType {
  id: string;
  type: string;
}

export interface TransitRouteResponse {
  id: string;
  code: string;
  displayName: string;
  routeType: TransitRouteType;
  fromLocation: string;
  toLocation: string;
}

