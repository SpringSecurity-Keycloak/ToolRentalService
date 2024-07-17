package io.rentalapp.service;

import io.rentalapp.common.DataFormat;
import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.holiday.ObservedHoliday;
import io.rentalapp.holiday.Weekend;
import io.rentalapp.persist.entity.RentalAgreementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RentalDurationService {

    private static final Logger log = LoggerFactory.getLogger(RentalDurationService.class);

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

        startDate.datesUntil(endDate)
                .forEach(rentalDay -> {
                    rentalPeriod.getDateRange().add(rentalDay);
                    rentalPeriod.setDueDate(DataFormat.toDate(rentalDay));

                    if (weekend.isWeekend(rentalDay)) {
                        int totalWeekendDays = rentalPeriod.getTotalWeekendDays();
                        rentalPeriod.setTotalWeekendDays(++totalWeekendDays);
                    }

                    /*
                     * if current date is a holiday and does not fall on a weekend, add it to the holiday count
                     */
                    if (observedHoliday.isWeekday(rentalDay)) {
                        int totalHolidays = rentalPeriod.getTotalHolidays();
                        rentalPeriod.setTotalHolidays(++totalHolidays);
                    }

                    LocalDate prevDay = rentalDay.minus(Period.ofDays(1));

                    /*
                     * if the previous day was a holiday and falls on a weekend, increment holiday count
                     */
                    if (rentalPeriod.getDateRange().contains(prevDay) && observedHoliday.isWeekend(prevDay)) {
                        int totalHolidays = rentalPeriod.getTotalHolidays();
                        rentalPeriod.setTotalHolidays(++totalHolidays);
                    }

                });

        rentalPeriod.setTotalWeekDays(
                rentalPeriod.getDateRange().size()
                - (rentalPeriod.getTotalWeekendDays() + rentalPeriod.getTotalHolidays()) );

        return rentalPeriod;
    }

}
