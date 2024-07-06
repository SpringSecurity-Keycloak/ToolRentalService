package io.rentalapp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

public class HolidayService {

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);

    public DateRangeDetails calculateDatesForRental(Date checkoutDate, int checkoutDays) {

        LocalDate startDate = checkoutDate.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate();
        LocalDate endDate = LocalDate.from(startDate);
        endDate = endDate.plusDays(checkoutDays);

        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        final long weekDaysBetween = startDate.datesUntil(endDate)
                .filter(d -> !weekend.contains(d.getDayOfWeek()))
                .count();

        DateRangeDetails dateRangeDetails = DateRangeDetails.builder().totalWeekDays(weekDaysBetween)
                .totalWeekendDays(startDate.datesUntil(endDate).count() - weekDaysBetween)
                .build();
        return dateRangeDetails;
    }

    /**
     *
     * @param date
     * @return
     */
    public Date parseDate(String date) throws ParseException {

        return formatter.parse(date);
    }

}
