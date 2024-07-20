package io.rentalapp.service;

import io.rentalapp.api.model.RentalRequest;
import io.rentalapp.common.DataFormat;
import io.rentalapp.common.DateRangeDetails;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties")
public class RentalDurationServiceTestCase {

    @Autowired
    RentalAgreementService rentalAgreementService;

    double price = 1.99;

    @Test
    @Transactional
    public void checkoutOnWeekday() {
        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("01/01/2024"),4);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("01/01/2024"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("01/04/2024"),dueDate);
        assertEquals(0,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(0,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(4,dateRangeDetails.getTotalWeekDays().intValue());

    }

    @Test
    @Transactional
    public void checkoutOnWeekend() {
        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("01/03/2024"),4);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("01/03/2024"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("01/06/2024"),dueDate);
        assertEquals(0,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(1,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(3,dateRangeDetails.getTotalWeekDays().intValue());

    }

    @Test
    @Transactional
    public void checkoutOn4JulyWeek() {
        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("07/01/2024"),4);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("07/01/2024"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("07/04/2024"),dueDate);
        assertEquals(1,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(0,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(3,dateRangeDetails.getTotalWeekDays().intValue());

    }

    @Test
    @Transactional
    public void checkout4JulyFallsOnWeekend_1() {

        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("07/01/2021"),4);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("07/01/2021"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("07/04/2021"),dueDate);
        assertEquals(0,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekDays().intValue());

    }

    @Test
    @Transactional
    public void checkout4JulyFallsOnWeekend_2() {

        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("07/01/2021"),5);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("07/01/2021"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("07/05/2021"),dueDate);
        assertEquals(1,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekDays().intValue());

    }

    @Test
    @Transactional
    public void checkout4JulyFallsOnWeekend_3() {

        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("07/01/2020"),4);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("07/01/2020"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("07/04/2020"),dueDate);
        assertEquals(1,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(1,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekDays().intValue());

    }

    @Test
    @Transactional
    public void checkoutJulyFallsOnWeekend_4() {

        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("07/01/2020"),6);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("07/01/2020"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("07/06/2020"),dueDate);
        assertEquals(1,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(3,dateRangeDetails.getTotalWeekDays().intValue());

    }

    @Test
    @Transactional
    public void checkoutAfterLaborDay() {

        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("08/28/2024"),6);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("08/28/2024"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("09/02/2024"),dueDate);
        assertEquals(1,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(3,dateRangeDetails.getTotalWeekDays().intValue());

    }



    @Test
    @Transactional
    public void checkoutOnLaborDay() {
        RentalDurationService rentalDuration = new RentalDurationService();
        DateRangeDetails dateRangeDetails = rentalDuration.calculateDatesForRental(DataFormat.parseDate("09/02/2024"),8);
        LocalDate checkoutDate = getFirst(dateRangeDetails.getDateRange());
        LocalDate dueDate = getLast(dateRangeDetails.getDateRange());
        assertEquals(DataFormat.toLocalDate("09/02/2024"),checkoutDate);
        assertEquals(DataFormat.toLocalDate("09/09/2024"),dueDate);
        assertEquals(1,dateRangeDetails.getTotalHolidays().intValue());
        assertEquals(2,dateRangeDetails.getTotalWeekendDays().intValue());
        assertEquals(5,dateRangeDetails.getTotalWeekDays().intValue());


    }
    /**
     * Helper method to create test data
     * @param discount
     * @param checkoutDate
     * @param rentalDays
     * @param toolCode
     * @return
     */
    private RentalRequest createTestFixture(int discount, String checkoutDate, int rentalDays, String toolCode) {
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setDiscountPercent(discount);
        rentalRequest.setCheckoutDate(checkoutDate);
        rentalRequest.setRentailDaysCount(rentalDays);
        rentalRequest.setToolCode(toolCode);
        return rentalRequest;
    }

    /**
     *
     * @param dateRange
     * @return
     */
    private static LocalDate getFirst(List<LocalDate> dateRange) {
        return dateRange.get(0);
    }

    /**
     *
     * @param dateRange
     * @return
     */
    private static LocalDate getLast(List<LocalDate> dateRange) {
        return dateRange.get(dateRange.size()-1);
    }
}
