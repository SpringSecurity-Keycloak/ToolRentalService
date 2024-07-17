package io.rentalapp.holiday;

import java.time.LocalDate;
import java.time.Month;

public class FourthJuly implements IHoliday{

    private static Weekend weekend= new Weekend();

    /**
     * Check if the passed in date falls on 4th of July
     * @param date the date to check
     * @return true if the date falls on 4th of July, false otherwise
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        boolean isIndependenceDay = (date.getMonth() == Month.JULY && date.getDayOfMonth() == 4);
        return isIndependenceDay;
    }

    /**
     * Check if the holiday falls on a weekend
     *
     * @param date
     * @return true if the date is an observed holiday and falls on a weekend, false otherwise
     */
    @Override
    public boolean isWeekend(LocalDate date) {
        boolean isWeekend =  isHoliday(date) && weekend.isWeekend(date);
        return isWeekend;
    }

    /**
     * Check if the holiday falls on a weekday
     * * @param date
     * * @return true if the date is an observed holiday and falls on a weekday, false otherwise
     *
     * @param date
     * @return
     */
    @Override
    public boolean isWeekday(LocalDate date) {
        return isHoliday(date) && !weekend.isWeekend(date);
    }
}
