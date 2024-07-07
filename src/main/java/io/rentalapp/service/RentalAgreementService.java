package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.model.RentalRequest;
import io.rentalapp.persist.RentalAgreementRepository;
import io.rentalapp.persist.RentalRequestRepository;
import io.rentalapp.persist.model.RentalAgreementDTO;
import io.rentalapp.persist.model.RentalRequestDTO;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

@Service
public class RentalAgreementService {

    private static final Logger log = LoggerFactory.getLogger(RentalAgreementService.class);
    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    RentalAgreementRepository rentalAgreementRepository;

    /**
     * Creates a rental agreement based on a rental request
     * @param rentalRequest
     * @return
     */
    public RentalAgreementDTO createRentalAgreement(RentalRequest rentalRequest) throws ParseException {

        HolidayService holidayService = new HolidayService();
        Date checkoutDate = holidayService.parseDate(rentalRequest.getCheckoutDate());
        Date rentalDueDate = DateUtils.addDays(checkoutDate,rentalRequest.getRentailDaysCount());

        DateRangeDetails dateRangeDetails = holidayService.calculateDatesForRental(checkoutDate,rentalRequest.getRentailDaysCount());

        RentalRequestDTO rentRequest = rentalRequestRepository
                .save(RentalRequestDTO.builder()
                        .toolCode(rentalRequest.getToolCode())
                        .rentailDaysCount(rentalRequest.getRentailDaysCount())
                        .discountPercent(rentalRequest.getDiscountPercent())
                        .checkoutDate(checkoutDate)
                        .build());


        RentalAgreementDTO rentalAgreementDTO =  RentalAgreementDTO.builder()
                .toolCode(rentalRequest.getToolCode())
                .checkoutDate(checkoutDate)
                .discountPercent(BigDecimal.valueOf(10))
                .dueDate(rentalDueDate)
                .rentalRequest(rentRequest)
                .build();

        rentalAgreementDTO = rentalAgreementRepository.save(rentalAgreementDTO);

        // fetch all rental requests
        log.info("All Rental Requests found with findAll():");
        log.info("-------------------------------");
        rentalRequestRepository.findAll().forEach(tool -> {
            log.info(tool.toString());
        });
        log.info("");

        // fetch all rental agreements
        log.info("All Rental Agreements found with findAll():");
        log.info("-------------------------------");
        rentalAgreementRepository.findAll().forEach(agreement -> {
            log.info("Tool Code in agreement " + agreement.getRentalRequest().getToolCode());
        });
        log.info(dateRangeDetails.toString());
        log.info("");

        return rentalAgreementDTO;
    }

}
