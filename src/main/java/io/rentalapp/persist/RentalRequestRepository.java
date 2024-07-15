package io.rentalapp.persist;

import io.rentalapp.persist.entity.RentalRequestEntity;
import org.springframework.data.repository.CrudRepository;

public interface RentalRequestRepository extends CrudRepository<RentalRequestEntity, Long>{
    RentalRequestEntity findById(long id);
}
