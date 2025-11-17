import { AxiosResponse } from "axios";
import api from "../api";
import { ScheduleResponse, TransitTimeCondition } from "./schedule.types";
import { sanitizeRouteSlug } from "../route/route.utils";

export async function fetchScheduleBySlug(slug: string): Promise<AxiosResponse<ScheduleResponse>> {
    return await api.get(`/schedule/${sanitizeRouteSlug(slug)}`);
}

export const formatDaysOfWeek = (daysOfWeek: string[], conditions: TransitTimeCondition[]): TooltipTextItem[] => {
    const effectiveDays: TooltipTextItem[] = daysOfWeek.map(day => ({text: day}));
    if (conditions && conditions.length > 0) {
        const weekdayConditions =
            conditions.filter(c => !c.isPassengerAllowed && c.effectiveDaysOfWeek && c.effectiveDaysOfWeek.length > 0);

        weekdayConditions.forEach(c => {
            c.effectiveDaysOfWeek?.forEach(day => {
                const existingDay = effectiveDays.find(x => x.text == day);
                if (existingDay) {
                    existingDay.tooltipProps = {
                        content: c.condition
                    };
                } else {
                    effectiveDays.push(
                        {
                            text: day,
                            tooltipProps: {
                                content: c.condition
                            }
                        }
                    );
                }
            });
        });
    }
        

    return effectiveDays;
}

export interface TooltipTextItem {
    text: string;
    tooltipProps?: {
        content: string;
    };
}
