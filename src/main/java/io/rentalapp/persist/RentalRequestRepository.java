package io.rentalapp.persist;

import io.rentalapp.persist.model.RentalRequest;

import org.springframework.data.repository.CrudRepository;

public interface RentalRequestRepository extends CrudRepository<RentalRequest, Long>{
    RentalRequest findById(long id);
}
