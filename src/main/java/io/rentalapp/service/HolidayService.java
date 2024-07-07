package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.common.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

public class HolidayService {

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);

    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);

    public DateRangeDetails calculateDatesForRental(Date checkoutDate, int checkoutDays) {

        LocalDate startDate = checkoutDate.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate();
        LocalDate endDate = LocalDate.from(startDate);
        endDate = endDate.plusDays(checkoutDays);

        final DateRangeDetails dateRangeDetails = new DateRangeDetails();

        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        startDate.datesUntil(endDate)
                .forEach(d -> {
                    boolean isWeekend = weekend.contains(d.getDayOfWeek());
                    boolean isHoliday = HolidayService.isHoliday(d);
                    if (isWeekend) {
                        long totalWeekendDays = dateRangeDetails.getTotalWeekendDays();
                        dateRangeDetails.setTotalWeekendDays(++totalWeekendDays);
                    }
                    else if (isHoliday) {
                        long totalHolidays = dateRangeDetails.getTotalHolidays();
                        dateRangeDetails.setTotalHolidays(++totalHolidays);
                    }
                    else {
                        long totalWeekDays = dateRangeDetails.getTotalWeekDays();
                        dateRangeDetails.setTotalWeekDays(++totalWeekDays);
                    }

                });

        return dateRangeDetails;
    }

    /**
     * Helper function to check if a given date falls on Labor Day
     * @param date
     * @return true if the date is a labor day date
     */
    static final boolean isLaborDay(LocalDate date) {
        LocalDate laborDay = LocalDate.of(date.getYear(), date.getMonth(), 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.MONDAY));

        boolean isLaborDay = (date.getDayOfYear() == laborDay.getDayOfYear() && date.getMonth() == Month.SEPTEMBER);
        if (isLaborDay) {
            log.info(laborDay.toString());
            log.info("is labor day : " + isLaborDay);
        }
        return isLaborDay;
    }

    /**
     * Helper function to check if a given date falls on Independence Day
     * @param date
     * @return true if the date is a Independence day date
     */
    static final boolean isIndependenceDay(LocalDate date) {
        boolean isIndependenceDay = (date.getMonth() == Month.JULY && date.getDayOfMonth() == 4);
        if (isIndependenceDay) {
            log.info(date.toString());
            log.info("is Independence day : " + isIndependenceDay);
        }
        return isIndependenceDay;
    }

    /**
     *
     * @param date
     * @return
     */
    static final boolean isHoliday(LocalDate date) {
        return isIndependenceDay(date) || isLaborDay(date);
    }



    /**
     *
     * @param date
     * @return
     */
    public Date parseDate(String date)  {

        Date result = null;
        try {
            formatter.setLenient(false);
            result = formatter.parse(date);
        } catch (ParseException e) {
            throw new ValidationException("Expected date format is MM-dd-YYYY. for e.g 12-31-2024");
        }
        return result;
    }

    /**
     *
     * @param date
     * @return
     */
    public String format(Date date) {
        return formatter.format(date);
    }

}
