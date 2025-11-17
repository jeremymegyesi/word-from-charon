
/*** Convert 24-hour time (HH:mm:ss) to 12-hour format with AM/PM ***/

export const to12HourFormat = (time: string): string => {
  if (!time.match(/^\d{1,2}:\d{2}:\d{2}$/)) {
    return time; // return original if format is unexpected
  }
  const [hourStr, minuteStr] = time.split(':');
  let hour = parseInt(hourStr, 10);
  const minute = minuteStr;
  const ampm = hour >= 12 ? 'pm' : 'am';
  hour = hour % 12 || 12;
  return `${hour}:${minute} ${ampm}`;
};

export const isDateStrToday = (dateStr: string): boolean => {
  const date = new Date(dateStr + 'T00:00:00Z');
  const today = new Date();
  return date.getUTCFullYear() === today.getUTCFullYear() &&
         date.getUTCMonth() === today.getUTCMonth() &&
         date.getUTCDate() === today.getUTCDate();
}

export const isDayStrToday = (dayStr: string): boolean => {
  const normalized = dayStr.trim().toLowerCase();
  const today = new Date();

  // Compare against today's weekday name (long and short) using a locale (English here).
  // Using toLocaleDateString avoids hardcoding an array of weekdays.
  const weekdayLong = today.toLocaleDateString('en-US', { weekday: 'long' }).toLowerCase();
  const weekdayShort = today.toLocaleDateString('en-US', { weekday: 'short' }).toLowerCase();

  return normalized === weekdayLong || normalized === weekdayShort;
};