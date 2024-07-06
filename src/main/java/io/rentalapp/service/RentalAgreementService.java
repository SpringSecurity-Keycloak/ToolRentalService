package io.rentalapp.service;

import io.rentalapp.model.RentalAgreement;
import io.rentalapp.model.RentalRequest;
import io.rentalapp.persist.RentalAgreementRepository;
import io.rentalapp.persist.RentalRequestRepository;
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
    public io.rentalapp.persist.model.RentalAgreement createRentalAgreement(RentalRequest rentalRequest) {

        io.rentalapp.persist.model.RentalRequest rentRequest = rentalRequestRepository
                .save(io.rentalapp.persist.model.RentalRequest.builder()
                        .toolCode(rentalRequest.getToolCode())
                        .rentailDaysCount(rentalRequest.getRentailDaysCount())
                        .discountPercent(rentalRequest.getDiscountPercent())
                        .checkoutDate(new Date())
                        .build());

        io.rentalapp.persist.model.RentalAgreement rentalAgreement =  io.rentalapp.persist.model.RentalAgreement.builder()
                .toolCode(rentalRequest.getToolCode())
                .checkoutDate(new Date())
                .discountPercent(BigDecimal.valueOf(10))
                .build();

        rentalAgreement = rentalAgreementRepository.save(rentalAgreement);

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
        rentalAgreementRepository.findAll().forEach(tool -> {
            log.info(tool.toString());
        });
        log.info("");

        return rentalAgreement;
    }

}
