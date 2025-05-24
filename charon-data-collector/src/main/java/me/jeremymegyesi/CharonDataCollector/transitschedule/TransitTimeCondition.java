package me.jeremymegyesi.CharonDataCollector.transitschedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.SortedSet;

import lombok.Data;

@Data
public class TransitTimeCondition {
    private String condition;
    private Boolean isPassengerAllowed;
    private SortedSet<DayOfWeek> effectiveDaysOfWeek;
    private SortedSet<LocalDate> effectiveDates;

    public boolean equalsWithoutDates(TransitTimeCondition other) {
        if (other == null) {
            return false;
        }
        return this.condition.equals(other.condition)
                && this.isPassengerAllowed.equals(other.isPassengerAllowed);
    }
}
