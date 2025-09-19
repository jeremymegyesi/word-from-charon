import { AxiosInstance } from "axios";
import { TransitRoute } from "../route.types";
import { fetchRoutesEnrichWithDepartures, sanitizeRouteSlug } from "../route.utils";


export async function fetchRouteBySlug(api: AxiosInstance, slug: string): Promise<TransitRoute> {
  const [resp] = await fetchRoutesEnrichWithDepartures(api, 5, sanitizeRouteSlug(slug)) as TransitRoute[];
  return resp;
}
