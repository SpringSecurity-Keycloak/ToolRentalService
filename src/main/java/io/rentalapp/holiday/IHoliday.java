package io.rentalapp.holiday;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public interface IHoliday {

    /**
     * Check if the passed in date is an holiday
     * @param date the date to check
     * @return true if the date is an observed holiday, false otherwise
     */
    boolean isHoliday(LocalDate date);

    /**
     * Check if the holiday falls on a weekend
     * @param date
     * @return true if the date is an observed holiday and falls on a weekend, false otherwise
     */
    boolean isWeekend(LocalDate date);

    /**
     * Check if the holiday falls on a weekday
     *      * @param date
     *      * @return true if the date is an observed holiday and falls on a weekday, false otherwise
     * @param date
     * @return
     */
    boolean isWeekday(LocalDate date);
}
