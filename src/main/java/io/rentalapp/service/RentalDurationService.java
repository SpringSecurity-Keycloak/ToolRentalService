package io.rentalapp.service;

import io.rentalapp.common.DataFormat;
import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.holiday.IHoliday;
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

        DateRangeDetails rentalPeriod = new DateRangeDetails();
        IHoliday observedHoliday = new ObservedHoliday();
        Weekend weekendCheck = new Weekend();

        startDate.datesUntil(endDate)
                .forEach(currentDate -> {
                    rentalPeriod.getDateRange().add(currentDate);
                    rentalPeriod.setDueDate(DataFormat.toDate(currentDate));
                    LocalDate prevDay = currentDate.minus(Period.ofDays(1));

                    if (weekendCheck.isWeekend(currentDate)) {
                        int totalWeekendDays = rentalPeriod.getTotalWeekendDays();
                        rentalPeriod.setTotalWeekendDays(++totalWeekendDays);
                    }

                    /*
                     * if current date is a holiday that falls on a weekday, add it to the holiday count
                     */
                    int totalHolidays = rentalPeriod.getTotalHolidays();
                    if (observedHoliday.isWeekday(currentDate)) {
                        rentalPeriod.setTotalHolidays(++totalHolidays);
                    }

                    if (prevDay.isBefore(startDate)) {
                        //out of range
                        return;
                    }

                    if (observedHoliday.isWeekend(currentDate) && !weekendCheck.isWeekend(prevDay)) {
                        rentalPeriod.setTotalHolidays(++totalHolidays);
                    }

                    if (observedHoliday.isWeekend(prevDay) && !weekendCheck.isWeekend(currentDate)) {
                        rentalPeriod.setTotalHolidays(++totalHolidays);
                    }


                });

        rentalPeriod.setTotalWeekDays(
                rentalPeriod.getDateRange().size()
                - (rentalPeriod.getTotalWeekendDays() + rentalPeriod.getTotalHolidays()) );

        return rentalPeriod;
    }

}
