package io.rentalapp.service;

import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.api.model.RentalRequest;
import io.rentalapp.api.model.Tool;

import java.util.List;

public interface IRentalAgreementService {

    /**
     * Find all tools available for rent in the system
     * @return List of available tools
     */
    List<Tool> findAllTools();

    /**
     * Create a new rental agreement based on the rental request
     * @param rentalRequest The rental request
     * @return the new Rental Agreement
     */
    RentalAgreement createRentalAgreement(RentalRequest rentalRequest);

    /**
     * Find all rental agreements in the system
     * @return List of all rental agreements
     */
    List<RentalAgreement> findAllRentalAgreements();
}
