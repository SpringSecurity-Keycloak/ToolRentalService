package io.rentalapp.persist.model;

import io.rentalapp.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Builder
@Entity(name = "Tool")
@AllArgsConstructor
@NoArgsConstructor
public class ToolDTO extends BaseEntity {

    private String code = null;

    private String type = null;

    private String brand = null;
}
