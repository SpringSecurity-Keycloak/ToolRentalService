package io.rentalapp.persist.model;

import io.rentalapp.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity(name = "RentalAgreement")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RentalAgreement", uniqueConstraints = {@UniqueConstraint(columnNames = {"rentalRequestId"})})
public class RentalAgreementDTO extends BaseEntity {

        private String toolCode = null;

        private String toolType = null;

        private String toolBrand = null;

        private String rentalDays = null;

        private Date checkoutDate = null;

        private Date dueDate = null;

        private BigDecimal dailyCharge = null;

        private BigDecimal chargeDays = null;

        private BigDecimal preDiscountCharge = null;

        private BigDecimal discountPercent = null;

        private BigDecimal finalCharge = null;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "rentalRequest.id")
        private RentalRequestDTO rentalRequest = null;

}
