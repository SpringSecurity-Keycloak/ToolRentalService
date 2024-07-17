package io.rentalapp.holiday;

import java.time.LocalDate;
import java.util.List;

public class ObservedHoliday implements IHoliday{
    private static final List<IHoliday> observedHolidays = List.of(
            new FourthJuly(),
            new LaborDay());

    /**
     * Check if the passed in date falls on an observed holiday in the system
     * @param date the date to check
     * @return true if the date falls on an observed holiday in the system, false otherwise
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        return observedHolidays.stream().anyMatch(holiday -> holiday.isHoliday(date));
    }

}
