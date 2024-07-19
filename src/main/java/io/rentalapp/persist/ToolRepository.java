package io.rentalapp.persist;

import io.rentalapp.persist.entity.ToolEntity;
import org.springframework.data.repository.CrudRepository;

public interface ToolRepository extends CrudRepository<ToolEntity, Long> {

    ToolEntity findById(long id);

    Iterable<ToolEntity> findAll();
}
