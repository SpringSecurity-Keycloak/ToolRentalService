package io.rentalapp.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Rental price details about each tool available for rent
 */
@Schema(description = "Rental price details about each tool available for rent")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T15:29:49.511604531Z[GMT]")


public class ToolPricingDetails   {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("daily_charge")
  private BigDecimal dailyCharge = null;

  @JsonProperty("weekend_charge")
  private Boolean weekendCharge = null;

  @JsonProperty("holiday_charge")
  private Boolean holidayCharge = null;

  public ToolPricingDetails code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Unique identifier for a tool instance
   * @return code
   **/
  @Schema(required = true, description = "Unique identifier for a tool instance")
      @NotNull

    public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ToolPricingDetails dailyCharge(BigDecimal dailyCharge) {
    this.dailyCharge = dailyCharge;
    return this;
  }

  /**
   * Daily Charge of the tool
   * @return dailyCharge
   **/
  @Schema(required = true, description = "Daily Charge of the tool")
      @NotNull

    @Valid
    public BigDecimal getDailyCharge() {
    return dailyCharge;
  }

  public void setDailyCharge(BigDecimal dailyCharge) {
    this.dailyCharge = dailyCharge;
  }

  public ToolPricingDetails weekendCharge(Boolean weekendCharge) {
    this.weekendCharge = weekendCharge;
    return this;
  }

  /**
   * Is the tool chargable on weekends?
   * @return weekendCharge
   **/
  @Schema(required = true, description = "Is the tool chargable on weekends?")
      @NotNull

    public Boolean isWeekendCharge() {
    return weekendCharge;
  }

  public void setWeekendCharge(Boolean weekendCharge) {
    this.weekendCharge = weekendCharge;
  }

  public ToolPricingDetails holidayCharge(Boolean holidayCharge) {
    this.holidayCharge = holidayCharge;
    return this;
  }

  /**
   * Is the tool chargeable on observed holidays?
   * @return holidayCharge
   **/
  @Schema(required = true, description = "Is the tool chargeable on observed holidays?")
      @NotNull

    public Boolean isHolidayCharge() {
    return holidayCharge;
  }

  public void setHolidayCharge(Boolean holidayCharge) {
    this.holidayCharge = holidayCharge;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToolPricingDetails toolPricingDetails = (ToolPricingDetails) o;
    return Objects.equals(this.code, toolPricingDetails.code) &&
        Objects.equals(this.dailyCharge, toolPricingDetails.dailyCharge) &&
        Objects.equals(this.weekendCharge, toolPricingDetails.weekendCharge) &&
        Objects.equals(this.holidayCharge, toolPricingDetails.holidayCharge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, dailyCharge, weekendCharge, holidayCharge);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ToolPricingDetails {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    dailyCharge: ").append(toIndentedString(dailyCharge)).append("\n");
    sb.append("    weekendCharge: ").append(toIndentedString(weekendCharge)).append("\n");
    sb.append("    holidayCharge: ").append(toIndentedString(holidayCharge)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
