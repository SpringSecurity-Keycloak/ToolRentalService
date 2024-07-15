package io.rentalapp.persist.entity;

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
public class RentalAgreementEntity extends BaseEntity {

        private String toolCode = null;

        private String toolType = null;

        private String toolBrand = null;

        private Integer rentalDays = null;

        private Date checkoutDate = null;

        private Date dueDate = null;

        private BigDecimal dailyCharge = null;

        private Integer chargeDays = null;

        private BigDecimal preDiscountCharge = null;

        private BigDecimal discountPercent = null;

        private BigDecimal discountAmount = null;

        private BigDecimal finalCharge = null;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "rentalRequest.id")
        private RentalRequestEntity rentalRequest = null;

}
