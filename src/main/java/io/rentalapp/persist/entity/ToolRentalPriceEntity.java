package io.rentalapp.persist.entity;

import io.rentalapp.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@Entity(name = "ToolRentalPrice")
@AllArgsConstructor
@NoArgsConstructor
public class ToolRentalPriceEntity extends BaseEntity {

    private String toolType = null;

    private BigDecimal dailyCharge;

    private boolean isWeekDayChargeable;

    private boolean isWeekEndChargeable;

    private boolean isHolidayChargeable;

}
