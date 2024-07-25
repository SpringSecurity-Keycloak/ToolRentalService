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

        if (observedHoliday.isWeekday(rentalDay) || adjustForHolidayWeekend(rentalDay, startDate, endDate)) {
            //it's a holiday or maybe a holiday that falls on a weekend
            totalHolidays++;
        }

        totalWeekDays = dateRange.size() - (totalWeekendDays + totalHolidays);

    }

    /**
     * Merge with another DateRangeDetails object
     * @param another
     */
    public void combine(DateRangeDetails another) {
        dateRange.addAll(another.getDateRange());
        totalWeekendDays += another.getTotalWeekendDays();
        totalHolidays += another.getTotalHolidays();
        totalWeekDays += another.getTotalWeekDays();
    }

    /**
     * Does holiday fall on a weekend?
     * @param currentDate
     * @param startDate
     * @param endDate
     * @return
     */
    private boolean adjustForHolidayWeekend(LocalDate currentDate, LocalDate startDate, LocalDate endDate) {
        boolean adjustForWeekend = false;
        if (observedHoliday.isWeekend(currentDate)) {
            LocalDate adjustedObservedHoliday = observedHoliday.getAdjustedDate(currentDate);
            if (observedHoliday.isBetween(adjustedObservedHoliday, startDate, endDate)) {
                adjustForWeekend = true;
            }
        }

        return adjustForWeekend;
    }

}
