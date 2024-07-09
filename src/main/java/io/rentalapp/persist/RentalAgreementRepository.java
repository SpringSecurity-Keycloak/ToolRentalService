package io.rentalapp.persist;

import io.rentalapp.persist.model.RentalAgreementDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface RentalAgreementRepository extends CrudRepository<RentalAgreementDTO, Long> {
    RentalAgreementDTO findById(long id);

    //@Query(" from RentalAgreement where max(dueDate) > :expectedDueDate and max(checkoutDate) >: checkoutDateRequested and toolCode =:toolCode")
    //Iterable<RentalAgreementDTO> findRentalAgreementBetween(Date checkoutDateRequested, Date expectedDueDate, String toolCode);
}
