package io.rentalapp.persist;

import io.rentalapp.persist.entity.RentalAgreementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface RentalAgreementRepository extends CrudRepository<RentalAgreementEntity, Long> {
    RentalAgreementEntity findById(long id);

    @Query(" from RentalAgreement where checkoutDate >= :checkoutDateRequested and dueDate <= :dueDateRequested and toolCode =:toolCode")
    Iterable<RentalAgreementEntity> findRentalAgreementBetween(@Param("checkoutDateRequested") Date checkoutDateRequested, @Param("dueDateRequested") Date dueDateRequested, @Param("toolCode") String toolCode);

    @Override
    Iterable<RentalAgreementEntity> findAll();
}

