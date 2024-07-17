package io.rentalapp.holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

public class Weekend implements IHoliday{

    private static final Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    /**
     * Check if the passed in date is an holiday
     *
     * @param date the date to check
     * @return true if the date is falls on a weekend, false otherwise
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        return weekend.contains(date.getDayOfWeek());
    }

}
