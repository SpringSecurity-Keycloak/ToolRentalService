package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

public class HolidayService {



    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);

    public DateRangeDetails calculateDatesForRental(Date checkoutDate, int checkoutDays) {

        LocalDate startDate = checkoutDate.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate();
        LocalDate endDate = LocalDate.from(startDate);
        endDate = endDate.plusDays(checkoutDays);

        final DateRangeDetails rentalPeriod = new DateRangeDetails();
        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        startDate.datesUntil(endDate)
                .forEach(rentalDay -> {
                    boolean isWeekend = weekend.contains(rentalDay.getDayOfWeek());
                    boolean isHoliday = HolidayService.isHoliday(rentalDay);

                    if (isWeekend) {
                        long totalWeekendDays = rentalPeriod.getTotalWeekendDays();
                        rentalPeriod.setTotalWeekendDays(++totalWeekendDays);
                    }

                    if (isHoliday) {
                        long totalHolidays = rentalPeriod.getTotalHolidays();
                        rentalPeriod.setTotalHolidays(++totalHolidays);
                    }

                });

        rentalPeriod.setTotalWeekDays(
                checkoutDays
                - (rentalPeriod.getTotalWeekendDays() + rentalPeriod.getTotalHolidays()) );

        return rentalPeriod;
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





}
