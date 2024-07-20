package io.rentalapp.persist.entity;

import io.rentalapp.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@Entity(name = "RentalRequest")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RentalRequest", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class RentalRequestEntity extends BaseEntity {

    private String toolCode = null;

    @Min(value = 1,message = "Rental days should be a minimum of 1 day")
    private Integer rentalDaysCount = null;

    @Max(value=100,message = "Discount percent is not in the range 0-100")
    @Min(value = 0,message = "Discount percent is not in the range 0-100")
    private Integer discountPercent = null;

    private Date checkoutDate = null;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
    private RentalAgreementEntity rentalAgreement = null;


}
