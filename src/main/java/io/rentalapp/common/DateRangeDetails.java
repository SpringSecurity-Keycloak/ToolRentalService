package io.rentalapp.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDetails {
    Long totalWeekDays = Long.valueOf(0);
    Long totalWeekendDays  = Long.valueOf(0);
    Long totalHolidays = Long.valueOf(0);
    List<LocalDate> dateRange = new ArrayList<LocalDate>();
}
