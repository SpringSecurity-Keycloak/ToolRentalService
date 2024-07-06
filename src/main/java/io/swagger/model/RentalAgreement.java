package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * RentalAgreement
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T12:46:17.388586925Z[GMT]")


public class RentalAgreement   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tool_code")
  private String toolCode = null;

  @JsonProperty("tool_type")
  private String toolType = null;

  @JsonProperty("tool_brand")
  private String toolBrand = null;

  @JsonProperty("rental_days")
  private String rentalDays = null;

  @JsonProperty("checkout_date")
  private String checkoutDate = null;

  @JsonProperty("due_date")
  private String dueDate = null;

  @JsonProperty("daily_charge")
  private BigDecimal dailyCharge = null;

  @JsonProperty("charge_days")
  private BigDecimal chargeDays = null;

  @JsonProperty("pre_discount_charge")
  private BigDecimal preDiscountCharge = null;

  @JsonProperty("discount_percent")
  private BigDecimal discountPercent = null;

  @JsonProperty("final_charge")
  private BigDecimal finalCharge = null;

  public RentalAgreement id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
      @NotNull

    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RentalAgreement toolCode(String toolCode) {
    this.toolCode = toolCode;
    return this;
  }

  /**
   * Get toolCode
   * @return toolCode
   **/
  @Schema(description = "")
      @NotNull

    public String getToolCode() {
    return toolCode;
  }

  public void setToolCode(String toolCode) {
    this.toolCode = toolCode;
  }

  public RentalAgreement toolType(String toolType) {
    this.toolType = toolType;
    return this;
  }

  /**
   * Get toolType
   * @return toolType
   **/
  @Schema(description = "")
      @NotNull

    public String getToolType() {
    return toolType;
  }

  public void setToolType(String toolType) {
    this.toolType = toolType;
  }

  public RentalAgreement toolBrand(String toolBrand) {
    this.toolBrand = toolBrand;
    return this;
  }

  /**
   * Get toolBrand
   * @return toolBrand
   **/
  @Schema(description = "")
      @NotNull

    public String getToolBrand() {
    return toolBrand;
  }

  public void setToolBrand(String toolBrand) {
    this.toolBrand = toolBrand;
  }

  public RentalAgreement rentalDays(String rentalDays) {
    this.rentalDays = rentalDays;
    return this;
  }

  /**
   * Get rentalDays
   * @return rentalDays
   **/
  @Schema(description = "")
      @NotNull

    public String getRentalDays() {
    return rentalDays;
  }

  public void setRentalDays(String rentalDays) {
    this.rentalDays = rentalDays;
  }

  public RentalAgreement checkoutDate(String checkoutDate) {
    this.checkoutDate = checkoutDate;
    return this;
  }

  /**
   * Get checkoutDate
   * @return checkoutDate
   **/
  @Schema(description = "")
      @NotNull

    public String getCheckoutDate() {
    return checkoutDate;
  }

  public void setCheckoutDate(String checkoutDate) {
    this.checkoutDate = checkoutDate;
  }

  public RentalAgreement dueDate(String dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  /**
   * Get dueDate
   * @return dueDate
   **/
  @Schema(description = "")
      @NotNull

    public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public RentalAgreement dailyCharge(BigDecimal dailyCharge) {
    this.dailyCharge = dailyCharge;
    return this;
  }

  /**
   * Get dailyCharge
   * @return dailyCharge
   **/
  @Schema(description = "")
      @NotNull

    @Valid
    public BigDecimal getDailyCharge() {
    return dailyCharge;
  }

  public void setDailyCharge(BigDecimal dailyCharge) {
    this.dailyCharge = dailyCharge;
  }

  public RentalAgreement chargeDays(BigDecimal chargeDays) {
    this.chargeDays = chargeDays;
    return this;
  }

  /**
   * Get chargeDays
   * @return chargeDays
   **/
  @Schema(description = "")
      @NotNull

    @Valid
    public BigDecimal getChargeDays() {
    return chargeDays;
  }

  public void setChargeDays(BigDecimal chargeDays) {
    this.chargeDays = chargeDays;
  }

  public RentalAgreement preDiscountCharge(BigDecimal preDiscountCharge) {
    this.preDiscountCharge = preDiscountCharge;
    return this;
  }

  /**
   * Get preDiscountCharge
   * @return preDiscountCharge
   **/
  @Schema(description = "")
      @NotNull

    @Valid
    public BigDecimal getPreDiscountCharge() {
    return preDiscountCharge;
  }

  public void setPreDiscountCharge(BigDecimal preDiscountCharge) {
    this.preDiscountCharge = preDiscountCharge;
  }

  public RentalAgreement discountPercent(BigDecimal discountPercent) {
    this.discountPercent = discountPercent;
    return this;
  }

  /**
   * Get discountPercent
   * @return discountPercent
   **/
  @Schema(description = "")
      @NotNull

    @Valid
    public BigDecimal getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(BigDecimal discountPercent) {
    this.discountPercent = discountPercent;
  }

  public RentalAgreement finalCharge(BigDecimal finalCharge) {
    this.finalCharge = finalCharge;
    return this;
  }

  /**
   * Get finalCharge
   * @return finalCharge
   **/
  @Schema(description = "")
      @NotNull

    @Valid
    public BigDecimal getFinalCharge() {
    return finalCharge;
  }

  public void setFinalCharge(BigDecimal finalCharge) {
    this.finalCharge = finalCharge;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RentalAgreement rentalAgreement = (RentalAgreement) o;
    return Objects.equals(this.id, rentalAgreement.id) &&
        Objects.equals(this.toolCode, rentalAgreement.toolCode) &&
        Objects.equals(this.toolType, rentalAgreement.toolType) &&
        Objects.equals(this.toolBrand, rentalAgreement.toolBrand) &&
        Objects.equals(this.rentalDays, rentalAgreement.rentalDays) &&
        Objects.equals(this.checkoutDate, rentalAgreement.checkoutDate) &&
        Objects.equals(this.dueDate, rentalAgreement.dueDate) &&
        Objects.equals(this.dailyCharge, rentalAgreement.dailyCharge) &&
        Objects.equals(this.chargeDays, rentalAgreement.chargeDays) &&
        Objects.equals(this.preDiscountCharge, rentalAgreement.preDiscountCharge) &&
        Objects.equals(this.discountPercent, rentalAgreement.discountPercent) &&
        Objects.equals(this.finalCharge, rentalAgreement.finalCharge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, toolCode, toolType, toolBrand, rentalDays, checkoutDate, dueDate, dailyCharge, chargeDays, preDiscountCharge, discountPercent, finalCharge);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RentalAgreement {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    toolCode: ").append(toIndentedString(toolCode)).append("\n");
    sb.append("    toolType: ").append(toIndentedString(toolType)).append("\n");
    sb.append("    toolBrand: ").append(toIndentedString(toolBrand)).append("\n");
    sb.append("    rentalDays: ").append(toIndentedString(rentalDays)).append("\n");
    sb.append("    checkoutDate: ").append(toIndentedString(checkoutDate)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    dailyCharge: ").append(toIndentedString(dailyCharge)).append("\n");
    sb.append("    chargeDays: ").append(toIndentedString(chargeDays)).append("\n");
    sb.append("    preDiscountCharge: ").append(toIndentedString(preDiscountCharge)).append("\n");
    sb.append("    discountPercent: ").append(toIndentedString(discountPercent)).append("\n");
    sb.append("    finalCharge: ").append(toIndentedString(finalCharge)).append("\n");
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
