package io.rentalapp.persist;

import io.rentalapp.persist.entity.ToolRentalPriceEntity;
import org.springframework.data.repository.CrudRepository;

public interface ToolRentalPriceRepositorty extends CrudRepository<ToolRentalPriceEntity, Long> {
    Iterable<ToolRentalPriceEntity> findAll();
}
