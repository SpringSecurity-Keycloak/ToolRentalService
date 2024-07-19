package io.rentalapp.persist;

import io.rentalapp.persist.entity.ToolRentalPriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ToolRentalPriceRepositorty extends CrudRepository<ToolRentalPriceEntity, Long> {
    Iterable<ToolRentalPriceEntity> findAll();

    @Query(" from ToolRentalPrice where toolType =:toolTypeCode")
    ToolRentalPriceEntity findByCode(@Param("toolTypeCode")String toolTypeCode);
}
