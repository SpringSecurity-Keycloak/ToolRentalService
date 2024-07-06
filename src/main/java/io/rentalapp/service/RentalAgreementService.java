package io.rentalapp.service;

import io.rentalapp.model.RentalRequest;
import io.rentalapp.persist.RentalAgreementRepository;
import io.rentalapp.persist.RentalRequestRepository;
import io.rentalapp.persist.model.RentalAgreementDTO;
import io.rentalapp.persist.model.RentalRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public RentalAgreementDTO createRentalAgreement(RentalRequest rentalRequest) {

        RentalRequestDTO rentRequest = rentalRequestRepository
                .save(RentalRequestDTO.builder()
                        .toolCode(rentalRequest.getToolCode())
                        .rentailDaysCount(rentalRequest.getRentailDaysCount())
                        .discountPercent(rentalRequest.getDiscountPercent())
                        .checkoutDate(new Date())
                        .build());

        RentalAgreementDTO rentalAgreementDTO =  RentalAgreementDTO.builder()
                .toolCode(rentalRequest.getToolCode())
                .checkoutDate(new Date())
                .discountPercent(BigDecimal.valueOf(10))
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
        log.info("");

        return rentalAgreementDTO;
    }

}
