package io.rentalapp.service;

import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.api.model.RentalRequest;
import io.rentalapp.common.DecimalNumber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties")
public class RentalAgreementServiceJAKDTestCase {

    @Autowired
    RentalAgreementService rentalAgreementService;

    double price = 2.99;

    @Test
    @Transactional
    public void checkoutOnWeekday() {
        RentalRequest rentalRequest = this.createTestFixture(0, "01/01/2024",4,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/04/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutOnWeekdayWithDiscount() {
        RentalRequest rentalRequest = this.createTestFixture(10, "01/01/2024",4,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/04/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(10.76),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutOnWeekend() {
        RentalRequest rentalRequest = this.createTestFixture(0, "01/03/2024",4,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/06/2024",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutOn4JulyWeek() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2024",4,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/04/2024",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkout4JulyWeek() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2024",4,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/04/2024",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkout4JulyFallsOnWeekend_1() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2021",4,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/04/2021",rentalAgreement.getDueDate());
        assertEquals(2,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkout4JulyFallsOnWeekend_2() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2021",5,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/05/2021",rentalAgreement.getDueDate());
        assertEquals(2,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutJulyFallsOnWeekend_3() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2020",6,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/06/2020",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutAfterLaborDay() {
        RentalRequest rentalRequest = this.createTestFixture(0, "08/28/2024",7,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("09/03/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutDueOnLaborDay() {
        RentalRequest rentalRequest = this.createTestFixture(0, "08/28/2024",6,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("09/02/2024",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutOnLaborDay() {
        RentalRequest rentalRequest = this.createTestFixture(0, "09/02/2024",8,"JAKD");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("09/09/2024",rentalAgreement.getDueDate());
        assertEquals(5,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

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
}
