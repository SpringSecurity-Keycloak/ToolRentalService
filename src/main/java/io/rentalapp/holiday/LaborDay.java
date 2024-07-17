package io.rentalapp.holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class LaborDay implements  IHoliday{

    /**
     * Check if the passed in date falls on Labor Day
     * @param date the date to check
     * @return true if the date falls on Labor Day, false otherwise
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        LocalDate laborDay = LocalDate.of(date.getYear(), date.getMonth(), 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.MONDAY));

        boolean isLaborDay = (date.getDayOfYear() == laborDay.getDayOfYear() && date.getMonth() == Month.SEPTEMBER);
        return isLaborDay;
    }
}
