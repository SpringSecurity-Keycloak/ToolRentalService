package io.rentalapp.holiday;

import java.time.LocalDate;
import java.util.List;

public class ObservedHoliday implements IHoliday{
    private static final List<IHoliday> observedHolidays = List.of(
            new FourthJuly(),
            new LaborDay());

    private Weekend weekendCheck = new Weekend();

    /**
     * Check if the passed in date falls on an observed holiday in the system
     * @param date the date to check
     * @return true if the date falls on an observed holiday in the system, false otherwise
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        return observedHolidays.stream().anyMatch(holiday -> holiday.isHoliday(date));
    }

    /**
     * Check if the holiday falls on a weekend
     *
     * @param date
     * @return true if the date is an observed holiday and falls on a weekend, false otherwise
     */
    @Override
    public boolean isWeekend(LocalDate date) {
        boolean isWeekend =  observedHolidays.stream().anyMatch(holiday ->  holiday.isWeekend(date));
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
        return observedHolidays.stream().anyMatch(holiday ->  holiday.isWeekday(date));
    }

    /**
     *
     * @param currentDate
     * @return
     */
    public LocalDate getActualDate(LocalDate currentDate) {
        LocalDate adjustedDate = LocalDate.from(currentDate);

        adjustedDate = isObservedOnNextWeekDay(currentDate) ? adjustedDate.plusDays(1) : adjustedDate;
        adjustedDate = isObservedOnPrevWeekDay(currentDate) ? adjustedDate.minusDays(1) : adjustedDate;

        return adjustedDate;
    }

    /**
     * Check if the current date is weekend and the previous day is a weekday
     * @param currentDate
     * @return
     */
    private boolean isObservedOnPrevWeekDay(LocalDate currentDate) {
        LocalDate prevDay = currentDate.minusDays(1);
        return isWeekend(currentDate) && !weekendCheck.isWeekend(prevDay) ;
    }

    /**
     * Check if the current date is weekend and the next day is a weekday
     * @param currentDate
     * @return
     */
    private boolean isObservedOnNextWeekDay(LocalDate currentDate) {
        LocalDate nextDay = currentDate.plusDays(1);
        return isWeekend(currentDate) && !weekendCheck.isWeekday(nextDay) ;
    }


}
