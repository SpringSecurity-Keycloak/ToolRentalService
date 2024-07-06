package io.rentalapp.persist.model;

import io.rentalapp.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RentalRequestDTO extends BaseEntity {

    private String toolCode = null;

    private Integer rentailDaysCount = null;

    private Integer discountPercent = null;

    private Date checkoutDate = null;

}
