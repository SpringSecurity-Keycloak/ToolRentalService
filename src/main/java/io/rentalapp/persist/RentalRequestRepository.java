package io.rentalapp.persist;

import io.rentalapp.persist.model.RentalRequestDTO;

import org.springframework.data.repository.CrudRepository;

public interface RentalRequestRepository extends CrudRepository<RentalRequestDTO, Long>{
    RentalRequestDTO findById(long id);
}
