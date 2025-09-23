import { AxiosInstance } from "axios";
import { TransitRoute } from "../route.types";
import { fetchDeparturesEnrichRoutes, fetchRoutesEnrichWithDepartures, sanitizeRouteSlug } from "../route.utils";


export async function fetchRouteBySlug(api: AxiosInstance, slug: string, departureCount?: number): Promise<TransitRoute> {
  const [resp] = await fetchRoutesEnrichWithDepartures(api, sanitizeRouteSlug(slug), departureCount) as TransitRoute[];
  return resp;
}

export async function fetchAndUpdateDepartureTimes(api: AxiosInstance, route: TransitRoute, departureCount?: number): Promise<TransitRoute> {
  const [resp] = await fetchDeparturesEnrichRoutes(api, [route], departureCount) as TransitRoute[];
  return resp;
}

export function isPastTime(timeStr: string, currTime: Date) {
  // Parse the time string using a regular expression
  const match = timeStr.match(/^(\d{1,2}):(\d{2}) (am|pm)$/i);
  if (!match) {
    throw new Error("Invalid time format. Use 'h:mm am' or 'h:mm pm'");
  }

  let [_, hourStr, minuteStr, period] = match;
  let hour = parseInt(hourStr, 10);
  const minute = parseInt(minuteStr, 10);

  // Convert to 24-hour format
  if (period.toLowerCase() === "pm" && hour !== 12) {
    hour += 12;
  } else if (period.toLowerCase() === "am" && hour === 12) {
    hour = 0;
  }

  // Remove sec & ms from currTime, only want to update after the minute changes
  currTime.setSeconds(0);
  currTime.setMilliseconds(0);

  // Create a Date object for today's target time
  const target = new Date(currTime);
  target.setHours(hour, minute, 0, 0);

  // Compare current time with target time
  return currTime > target;
}
