package io.rentalapp.persist;

import io.rentalapp.persist.model.ToolRentalPriceDTO;
import org.springframework.data.repository.CrudRepository;

public interface ToolRentalPriceRepositorty extends CrudRepository<ToolRentalPriceDTO, Long> {
    Iterable<ToolRentalPriceDTO> findAll();
}
