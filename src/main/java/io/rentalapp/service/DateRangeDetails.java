package io.rentalapp.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDetails {
    Long totalWeekDays = Long.valueOf(0);
    Long totalWeekendDays  = Long.valueOf(0);
    Long totalHolidays = Long.valueOf(0);
}
