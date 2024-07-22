package io.rentalapp.service;

import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.api.model.RentalRequest;
import io.rentalapp.common.DecimalNumber;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties")
public class RentalAgreementServiceCHNSTestCase {
    @Autowired
    RentalAgreementService rentalAgreementService;

    double price = 1.49;

    @Test
    @Transactional
    public void checkoutChainsawOnWeekday() {
        RentalRequest rentalRequest = this.createTestFixture(0, "01/01/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/04/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsawOnWeekdayWithDiscount() {
        RentalRequest rentalRequest = this.createTestFixture(10, "01/01/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/04/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(5.36),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsawWithWeekend() {
        RentalRequest rentalRequest = this.createTestFixture(0, "01/03/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/06/2024",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyWeek() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/04/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyFallsOnWeekend_1() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2021",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/04/2021",rentalAgreement.getDueDate());
        assertEquals(2,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyFallsOnWeekend_2() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2021",5,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/05/2021",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyFallsOnWeekend_3() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2020",6,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/06/2020",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyFallsOnWeekend_4() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/04/2024",1,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/04/2024",rentalAgreement.getDueDate());
        assertEquals(1,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(price*rentalAgreement.getChargeDays().intValue()),rentalAgreement.getFinalCharge());
    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyFallsOnWeekend_5() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/04/2020",1,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/04/2020",rentalAgreement.getDueDate());
        assertEquals(0,rentalAgreement.getChargeDays().intValue());
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

    @Test
    @Transactional
    public void checkoutChainsaw4JulyFallsOnWeekend_print() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2021",5,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        rentalAgreement.print();

    }
}
