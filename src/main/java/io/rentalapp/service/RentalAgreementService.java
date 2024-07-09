package io.rentalapp.service;

import io.rentalapp.common.DateRangeDetails;
import io.rentalapp.common.DateUtility;
import io.rentalapp.common.ValidationException;
import io.rentalapp.model.RentalRequest;
import io.rentalapp.persist.RentalAgreementRepository;
import io.rentalapp.persist.RentalRequestRepository;
import io.rentalapp.persist.ToolRepository;
import io.rentalapp.persist.model.RentalAgreementDTO;
import io.rentalapp.persist.model.RentalRequestDTO;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class RentalAgreementService {

    private static final Logger log = LoggerFactory.getLogger(RentalAgreementService.class);
    DateUtility dateUtility = new DateUtility();

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    ToolRepository toolRepository;

    Set<String> validToolCodes = new HashSet<String>();

    /**
     * Creates a rental agreement based on a rental request
     * @param rentalRequest
     * @return
     */
    public RentalAgreementDTO createRentalAgreement(RentalRequest rentalRequest) {

        if (!isValidToolCode(rentalRequest.getToolCode())) {
            throw new ValidationException("Invalid Tool Code Entered");
        }

        Date checkoutDate = dateUtility.parseDate(rentalRequest.getCheckoutDate());
        Date rentalDueDate = DateUtils.addDays(checkoutDate,rentalRequest.getRentailDaysCount());

        /*
         * Read the last rental agreement created for the requested tool to determine if the tool is
         * available for rental
         */

        /*
         * Determine number of weekdays and holidays in the requested rental period
         */
        HolidayService holidayService = new HolidayService();
        DateRangeDetails dateRangeDetails = holidayService.calculateDatesForRental(checkoutDate,rentalRequest.getRentailDaysCount());

        /*
         * Get pricing rules per tool
         */

        /*
         * Calculate the pricing based on the days requested and the pricing rules
         * for the tool
         */

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

    /**
     *
     * @param toolCode
     * @return
     */
    boolean isValidToolCode(String toolCode) {

        if (validToolCodes.isEmpty()) {
            toolRepository.findAll().forEach(tool -> validToolCodes.add(tool.getCode()));
        }
        return validToolCodes.contains(toolCode);
    }

}
