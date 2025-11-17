import { Fragment } from "react/jsx-runtime";
import { Breadcrumb, BreadcrumbItem, BreadcrumbList, BreadcrumbSeparator, Button, Separator, Spinner, Table, TableBody, TableCell, TableHead, TableHeader, TableRow, Tooltip } from "../../../react-charon-lib";
import { isDateStrToday, isDayStrToday, to12HourFormat } from "../../timeUtils";
import { ScheduleData, TerminalSchedule, TransitTime } from "../schedule.types";
import { formatDaysOfWeek } from "../schedule.utils";
import { ArrowLeft } from "lucide-react";

interface RouteInsightsProps {
  schedule: ScheduleData | undefined;
  loading?: boolean;
  substituteLabels: Map<string, string>;
  routeCode: string | undefined;
  routeTitle: string | undefined;
  nextDepartures?: Record<string, string | undefined>;
  navBack?: () => void;
}

const renderTerminalSchedule = (terminalData: TerminalSchedule) => {
  return (
    <div key={terminalData.terminal}>
      <h2 className="text-xl font-bold mb-1 text-primary">{`${terminalData.terminal} Terminal`}</h2>
      <Table className="border table-alternating-row">
        <TableHeader>
          <TableRow className="bg-primary-dull text-primary-foreground" >
            <TableHead>Departure</TableHead>
            <TableHead>Arrival</TableHead>
            <TableHead>Excluded Days of Week</TableHead>
            <TableHead>Excluded Dates</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {terminalData.transitTimes.map((u: TransitTime) => (
            <TableRow key={`${u.departure}-${u.arrival}`} className={u.classNames}>
              <TableCell className="py-0">{to12HourFormat(u.departure)}</TableCell>
              <TableCell className="py-0">{to12HourFormat(u.arrival)}</TableCell>
              <TableCell className="py-0" style={{ fontStyle: 'italic' }}>
                {
                  formatDaysOfWeek(u.excludedDaysOfWeek, u.transitTimeConditions)
                    .map((x, idx) => {
                      const keyBase = `tooltip-${terminalData.terminal}-${u.departure}-${idx}`;
                      if (!x.tooltipProps?.content) {
                        return <span key={keyBase}>{x.text}</span>;
                      }
                      return (
                        <Tooltip key={keyBase} tooltipKey={keyBase.replace(/[^a-zA-Z0-9]/g, '')} content={x.tooltipProps?.content}>
                          {x.text}
                        </Tooltip>
                      );
                    })
                    .reduce(
                      (acc: React.ReactNode[], current: React.ReactNode, index: number) =>
                        index ? [...acc, <Fragment key={`sep-${terminalData.terminal}-${u.departure}-${index}`}>{', '}</Fragment>, current] : [...acc, current], []
                    )
                }
              </TableCell>
              <TableCell className="p-0" style={{ fontStyle: 'italic' }}>
                {
                  u.excludedDates.map(x => new Date(x).toLocaleDateString('en-US', { timeZone: 'UTC', month: 'short', day: 'numeric' })).join(', ')
                }
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}

const addRowClasses = (schedule: TerminalSchedule, nextDeparture: string | undefined) => {
  schedule.transitTimes.forEach(tt => {
    if (tt.excludedDates.some(dateStr => isDateStrToday(dateStr)) || tt.excludedDaysOfWeek.some(dayStr => isDayStrToday(dayStr)) ||
        tt.transitTimeConditions.some(cond => !cond.isPassengerAllowed && (cond.effectiveDates?.some(dateStr => isDateStrToday(dateStr)) || cond.effectiveDaysOfWeek?.some(dayStr => isDayStrToday(dayStr))))
      ) {
      tt.classNames = 'cancelled-departure-row';
    } else if (nextDeparture && nextDeparture === tt.departure) {
      tt.classNames = 'next-departure-row';
    }
    else {
      tt.classNames = '';
    }
  });

  return schedule;
}

export default function RouteInsights({ schedule, loading, substituteLabels, routeCode, routeTitle, nextDepartures, navBack }: RouteInsightsProps) {
  const terminalSchedules = schedule &&
  [
    schedule.onwardSchedule,
    schedule.returnSchedule
  ] || [];

  return (
      <>
        <div className="container mx-auto px-4 py-4 max-w-6xl bg-gradient-to-r from-primary-accent-soft to-transparent">
          <div className="flex items-center gap-2">
            {/* Back button to previous page */}
            <Button onClick={navBack} aria-label="Go back">
              <ArrowLeft />
              Back
            </Button>

            <Separator orientation="vertical" className="h-6" />

            {/* Example usage of the Breadcrumb component */}
            <Breadcrumb aria-label="Breadcrumb">
            <BreadcrumbList>
              <BreadcrumbItem>
                <a href="/" className="text-sm text-muted-foreground hover:underline">Routes</a>
              </BreadcrumbItem>
              <BreadcrumbSeparator />
              <BreadcrumbItem>
                <a href={`/${routeCode}/insights`} className="text-sm text-muted-foreground hover:underline">{routeCode ? routeCode : ''} Insights</a>
              </BreadcrumbItem>
              <BreadcrumbSeparator />
              <span className="text-sm font-medium">{routeCode ? `${routeCode} Schedule` : 'Schedule'}</span>
            </BreadcrumbList>
            </Breadcrumb>
          </div>
        </div>
        {
          loading ? <Spinner /> : (
          <div className="container mx-auto px-4 py-6 max-w-6xl">
            <p className="text-lg">Schedule Information for <u>{routeTitle}</u></p>
            <p className="mb-5 text-sm italic text-muted-foreground">Schedule data last updated: {new Date(schedule?.collectedOn || '').toLocaleString(['en-US'], {day: "numeric", month: "numeric", year: "numeric"})}</p>
            <div className="flex flex-wrap gap-4">
              {terminalSchedules.length > 0 ? 
              (
                terminalSchedules.map((terminalData) =>
                  renderTerminalSchedule({
                    ...addRowClasses(terminalData, nextDepartures && nextDepartures[terminalData.terminal] || undefined),
                    terminal: substituteLabels.get(terminalData.terminal) ?? terminalData.terminal
                  })
                )
              ) : (
                <p>No schedule data available.</p>
              )}
            </div>
          </div>
          )
        }
      </>
    );
}