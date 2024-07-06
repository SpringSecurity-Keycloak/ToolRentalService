package io.rentalapp.persist;

import io.rentalapp.persist.model.RentalAgreement;
import org.springframework.data.repository.CrudRepository;

public interface RentalAgreementRepository extends CrudRepository<RentalAgreement, Long> {
    RentalAgreement findById(long id);
}
