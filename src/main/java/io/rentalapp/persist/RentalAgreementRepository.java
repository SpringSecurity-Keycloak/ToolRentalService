package io.rentalapp.persist;

import io.rentalapp.persist.model.RentalAgreementDTO;
import org.springframework.data.repository.CrudRepository;

public interface RentalAgreementRepository extends CrudRepository<RentalAgreementDTO, Long> {
    RentalAgreementDTO findById(long id);
}
