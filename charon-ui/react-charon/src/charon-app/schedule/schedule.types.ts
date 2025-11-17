export interface TransitTime {
    departure: string;
    arrival: string;
    excludedDaysOfWeek: string[];
    excludedDates: string[];
    transitTimeConditions: TransitTimeCondition[];
    classNames?: string;
};

export interface TransitTimeCondition {
    condition: string;
    isPassengerAllowed: boolean;
    effectiveDaysOfWeek: string[] | null;
    effectiveDates: string[] | null;
};

export interface ScheduleResponse {
    id: string;
    collectedOn: string;
    scheduleData: ScheduleData;
};

export interface ScheduleData {
    onwardSchedule: TerminalSchedule;
    returnSchedule: TerminalSchedule;
    collectedOn: string;
};

export interface TerminalSchedule {
    terminal: string;
    transitTimes: TransitTime[];
};