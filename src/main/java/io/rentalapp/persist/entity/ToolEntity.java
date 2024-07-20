package io.rentalapp.persist.entity;

import io.rentalapp.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Builder
@Entity(name = "Tool")
@AllArgsConstructor
@NoArgsConstructor
public class ToolEntity extends BaseEntity {

    private String code = null;

    private String type = null;

    private String brand = null;
}
