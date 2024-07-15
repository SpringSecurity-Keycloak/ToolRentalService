package io.rentalapp.service;

import io.rentalapp.BootStrap;
import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.common.DataFormat;
import io.rentalapp.common.DecimalNumber;
import io.rentalapp.api.model.RentalRequest;
import io.rentalapp.persist.entity.RentalAgreementEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties")
public class RentalAgreementServiceTestCase {
    @Autowired
    RentalAgreementService rentalAgreementService;

    @Autowired
    BootStrap bootStrap;

    @Test
    @Transactional
    public void checkoutChainsawOnWeekday() {
        RentalRequest rentalRequest = this.createTestFixture(0, "01/01/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/05/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(2.99*4),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsawOnWeekdayWithDiscount() {
        RentalRequest rentalRequest = this.createTestFixture(10, "01/01/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/05/2024",rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(10.76),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsawWithWeekend() {
        RentalRequest rentalRequest = this.createTestFixture(0, "01/03/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("01/07/2024",rentalAgreement.getDueDate());
        assertEquals(2,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(5.98),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyWeek() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2024",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/05/2024",rentalAgreement.getDueDate());
        assertEquals(3,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(8.97),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsaw4JulyFallsOnWeekend() {
        RentalRequest rentalRequest = this.createTestFixture(0, "07/01/2021",4,"CHNS");
        RentalAgreement rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals("07/05/2021",rentalAgreement.getDueDate());
        assertEquals(1,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(2.99),rentalAgreement.getFinalCharge());

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
