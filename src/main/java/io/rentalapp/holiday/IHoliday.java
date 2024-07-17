package io.rentalapp.holiday;

import java.time.LocalDate;

public interface IHoliday {

    /**
     * Check if the passed in date is an holiday
     * @param date the date to check
     * @return true if the date is an observed holiday, false otherwise
     */
    boolean isHoliday(LocalDate date);
}
