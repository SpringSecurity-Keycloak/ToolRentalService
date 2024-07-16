package io.rentalapp.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to hold the date related details of the rental agreement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDetails {
    Integer totalWeekDays = Integer.valueOf(0);
    Integer totalWeekendDays  = Integer.valueOf(0);
    Integer totalHolidays = Integer.valueOf(0);
    List<LocalDate> dateRange = new ArrayList<LocalDate>();
    Date dueDate = null;
}
