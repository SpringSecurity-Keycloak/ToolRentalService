package io.rentalapp.holiday;

import java.time.LocalDate;
import java.time.Month;

public class FourthJuly implements IHoliday{

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
}
