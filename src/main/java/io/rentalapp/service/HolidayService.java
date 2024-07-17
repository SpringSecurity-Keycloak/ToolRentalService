package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.holiday.ObservedHoliday;
import io.rentalapp.holiday.Weekend;
import io.rentalapp.persist.entity.RentalAgreementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
        endDate = endDate.plusDays(checkoutDays);

        final DateRangeDetails rentalPeriod = new DateRangeDetails();
        Weekend weekend = new Weekend();
        ObservedHoliday observedHoliday = new ObservedHoliday();
        AtomicReference<LocalDate> prevDay = new AtomicReference<LocalDate>();

        startDate.datesUntil(endDate)
                .forEach(rentalDay -> {
                    rentalPeriod.getDateRange().add(rentalDay);
                    rentalPeriod.setDueDate(Date.from(rentalDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    boolean isWeekend = weekend.isHoliday(rentalDay);
                    boolean isHoliday = observedHoliday.isHoliday(rentalDay);

                    if (isWeekend) {
                        int totalWeekendDays = rentalPeriod.getTotalWeekendDays();
                        rentalPeriod.setTotalWeekendDays(++totalWeekendDays);
                    }

                    /*
                     * if current date is a holiday and does not fall on a weekend, add it to the holiday count
                     */
                    if (isHoliday && !isWeekend) {
                        int totalHolidays = rentalPeriod.getTotalHolidays();
                        rentalPeriod.setTotalHolidays(++totalHolidays);
                    }

                    /*
                     * if the previous day was a holiday and falls on a weekend, increment holiday count
                     */
                    if (prevDay.get() != null) {
                        if (weekend.isHoliday(prevDay.get()) && observedHoliday.isHoliday(prevDay.get())) {
                            int totalHolidays = rentalPeriod.getTotalHolidays();
                            rentalPeriod.setTotalHolidays(++totalHolidays);
                        }

                    }
                    prevDay.set(rentalDay);

                });

        rentalPeriod.setTotalWeekDays(
                rentalPeriod.getDateRange().size()
                - (rentalPeriod.getTotalWeekendDays() + rentalPeriod.getTotalHolidays()) );

        return rentalPeriod;
    }

}
