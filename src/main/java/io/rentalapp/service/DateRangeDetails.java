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
    Long totalWeekDays;
    Long totalWeekendDays;
    Long totalHolidays;
}
