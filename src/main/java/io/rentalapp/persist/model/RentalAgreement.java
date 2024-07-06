package io.rentalapp.persist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rentalapp.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RentalAgreement  extends BaseEntity {

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

}
