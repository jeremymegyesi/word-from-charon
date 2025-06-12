package me.jeremymegyesi.CharonCommon.transitschedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.SortedSet;

import lombok.Data;

@Data
public class TransitTime implements Comparable<TransitTime> {
    private LocalTime departure;
    private LocalTime arrival;
    private SortedSet<DayOfWeek> excludedDaysOfWeek;
    private SortedSet<LocalDate> excludedDates;
    private Set<TransitTimeCondition> transitTimeConditions;

    public boolean equalsWithoutDates(TransitTime other) {
        if (other == null) {
            return false;
        }
        return this.departure.equals(other.departure) && this.arrival.equals(other.arrival)
            && ((this.transitTimeConditions == null && other.transitTimeConditions == null)
                || (this.transitTimeConditions != null && this.transitTimeConditions.equals(other.transitTimeConditions)));
    }
    
    public boolean equalsWithTimes(TransitTime other) {
        if (other == null) {
            return false;
        }
        return this.departure.equals(other.departure) && this.arrival.equals(other.arrival);
    }

    @Override
    public int compareTo(TransitTime other) {
        return this.departure.compareTo(other.departure);
    }
}
