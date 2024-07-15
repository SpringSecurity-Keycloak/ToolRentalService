package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.persist.entity.RentalAgreementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class HolidayService {

    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);

    /**
     * Get a range of dates for the passed in rental Agreement
     * @param rentalAgreement
     * @return
     */
    public static List<LocalDate> getDateRange(RentalAgreementEntity rentalAgreement) {
        LocalDate startDate = rentalAgreement.getCheckoutDate().toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate();
        LocalDate endDate = rentalAgreement.getDueDate().toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate();

        return startDate.datesUntil(endDate).collect(Collectors.toList());
    }

    /**
     * Calculate
     * <p>
     *     <ul> The dates between the checkedDate and number of days to checkout in the rental agreement </ul>
     *     <ul> The number of weekdays in the rental agreement </ul>
     *     <ul> The number of weekends in the rental agreement </ul>
     *     <ul> The number of holidays in the rental agreement </ul>
     * </p>
     *
     * @param checkoutDate
     * @param checkoutDays
     * @return
     */
    public DateRangeDetails calculateDatesForRental(Date checkoutDate, int checkoutDays) {

        LocalDate startDate = checkoutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = LocalDate.from(startDate);
        endDate = endDate.plusDays(checkoutDays+1);

        final DateRangeDetails rentalPeriod = new DateRangeDetails();
        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        startDate.datesUntil(endDate)
                .forEach(rentalDay -> {
                    rentalPeriod.getDateRange().add(rentalDay);
                    boolean isWeekend = weekend.contains(rentalDay.getDayOfWeek());
                    boolean isHoliday = HolidayService.isHoliday(rentalDay);

                    if (isWeekend) {
                        int totalWeekendDays = rentalPeriod.getTotalWeekendDays();
                        rentalPeriod.setTotalWeekendDays(++totalWeekendDays);
                    }

                    if (isHoliday) {
                        int totalHolidays = rentalPeriod.getTotalHolidays();
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
     * Check if the passed in date falls under an observed holiday
     * @param date
     * @return
     */
    static final boolean isHoliday(LocalDate date) {
        return isIndependenceDay(date) || isLaborDay(date);
    }





}
