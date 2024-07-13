package io.rentalapp.service;

import io.rentalapp.BootStrap;
import io.rentalapp.common.DataFormat;
import io.rentalapp.common.DecimalNumber;
import io.rentalapp.model.RentalRequest;
import io.rentalapp.persist.model.RentalAgreementDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        RentalAgreementDTO rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals(DataFormat.parseDate("01/05/2024"),rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(2.99*4),rentalAgreement.getFinalCharge());

    }

    @Test
    @Transactional
    public void checkoutChainsawOnWeekdayWithDiscount() {
        RentalRequest rentalRequest = this.createTestFixture(10, "01/01/2024",4,"CHNS");
        RentalAgreementDTO rentalAgreement = rentalAgreementService.createRentalAgreement(rentalRequest);
        assertEquals(DataFormat.parseDate("01/05/2024"),rentalAgreement.getDueDate());
        assertEquals(4,rentalAgreement.getChargeDays().intValue());
        assertEquals(DecimalNumber.valueOf(10.76),rentalAgreement.getFinalCharge());

    }


    /**
     *
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
