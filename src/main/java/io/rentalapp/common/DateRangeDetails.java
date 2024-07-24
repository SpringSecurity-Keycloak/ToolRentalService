package io.rentalapp.common;

import io.rentalapp.holiday.ObservedHoliday;
import io.rentalapp.holiday.Weekend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class to hold the date related details of the rental agreement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDetails implements Consumer<LocalDate> {

    private Integer totalWeekDays = Integer.valueOf(0);
    private Integer totalWeekendDays  = Integer.valueOf(0);
    private Integer totalHolidays = Integer.valueOf(0);

    private List<LocalDate> dateRange = new ArrayList<LocalDate>();
    private Date dueDate = null;

    private LocalDate startDate;
    private LocalDate endDate;

    private Weekend weekendCheck = new Weekend();
    private ObservedHoliday observedHoliday = new ObservedHoliday();

    /**
     * Constructor that accepts the date range boundaries
     * @param aStartDate
     * @param aEndDate
     */
    public DateRangeDetails(LocalDate aStartDate,LocalDate aEndDate) {
        startDate = aStartDate;
        endDate = aEndDate;
    }

    /**
     * Accepts a rental day and increments the weekend / holiday count
     * @param rentalDay the input argument
     */
    public void accept(LocalDate rentalDay) {
        dateRange.add(rentalDay);

        if (weekendCheck.isWeekend(rentalDay)) {
            //it's a weekend.
            totalWeekendDays++;
        }

        if (isWeekdayHoliday(rentalDay) || adjustForHolidayWeekend(rentalDay, startDate, endDate)) {
            //it's a holiday or maybe a holiday that falls on a weekend
            totalHolidays++;
        }
    }

    /**
     * Merge with another DateRangeDetails object
     * @param another
     */
    public void combine(DateRangeDetails another) {
        dateRange.addAll(another.getDateRange());
        totalWeekendDays += another.getTotalWeekendDays();
        totalHolidays += another.getTotalHolidays();
    }

    /**
     * Does holiday fall on a weekday?
     * @param currentDate
     * @return
     */
    private boolean isWeekdayHoliday(LocalDate currentDate) {
        return observedHoliday.isWeekday(currentDate);
    }

    /**
     * Does holiday fall on a weekend?
     * @param currentDate
     * @param startDate
     * @param endDate
     * @return
     */
    private boolean adjustForHolidayWeekend(LocalDate currentDate, LocalDate startDate, LocalDate endDate) {
        boolean adjustedForWeekend = false;
        if (observedHoliday.isWeekend(currentDate)) {
            LocalDate adjustedObservedHoliday = observedHoliday.getAdjustedDate(currentDate);
            if (isBetween(adjustedObservedHoliday, startDate, endDate)) {
                adjustedForWeekend = true;
            }
        }

        return adjustedForWeekend;
    }

    /**
     *
     * @param adjustedObservedHoliday
     * @param startDate
     * @param endDate
     * @return
     */
    private  boolean isBetween(LocalDate adjustedObservedHoliday, LocalDate startDate, LocalDate endDate) {
        return !adjustedObservedHoliday.isBefore(startDate) &&
                !adjustedObservedHoliday.equals(endDate);
    }
}
