package io.rentalapp.persist;

import io.rentalapp.model.RentalAgreement;
import io.rentalapp.model.RentalRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    public RentalAgreement createRentalAgreement(RentalRequest rentalRequest) {
        rentalRequest = rentalRequestRepository.save(rentalRequest);

        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.toolCode(rentalRequest.getToolCode());
        rentalAgreement.checkoutDate("12-12-2023");
        rentalAgreement.discountPercent(BigDecimal.valueOf(10));


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
