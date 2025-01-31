package io.rentalapp.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rentalapp.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.annotation.processing.Generated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * RentalRequest
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T15:29:49.511604531Z[GMT]")


@AllArgsConstructor
@NoArgsConstructor
public class RentalRequest   extends BaseEntity {

  @NotNull(message = "Tool Code is required")
  @JsonProperty("tool_code")
  private String toolCode = null;

  @Min(value = 1,message = "Rental days should be a minimum of 1 day")
  @NotNull(message = "Rental Days is required")
  @JsonProperty("rental_days_count")
  private Integer rentailDaysCount = null;

  @JsonProperty("discount_percent")
  private Integer discountPercent = null;

  @NotNull(message = "Checkout Date is required")
  @JsonProperty("checkout_date")
  private String checkoutDate = null;

  public RentalRequest toolCode(String toolCode) {
    this.toolCode = toolCode;
    return this;
  }

  /**
   * Get toolCode
   * @return toolCode
   **/
  @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY, description = "")
  public String getToolCode() {
    return toolCode;
  }
  public void setToolCode(String toolCode) {
    this.toolCode = toolCode;
  }

  public RentalRequest rentailDaysCount(Integer rentailDaysCount) {
    this.rentailDaysCount = rentailDaysCount;
    return this;
  }

  /**
   * Get rentailDaysCount
   * @return rentailDaysCount
   **/
  @Schema(required = true, description = "")
  public Integer getRentailDaysCount() {
    return rentailDaysCount;
  }

  public void setRentailDaysCount(Integer rentailDaysCount) {
    this.rentailDaysCount = rentailDaysCount;
  }

  public RentalRequest discountPercent(Integer discountPercent) {
    this.discountPercent = discountPercent;
    return this;
  }

  /**
   * Get discountPercent
   * minimum: 0
   * maximum: 100
   * @return discountPercent
   **/
  @Schema(description = "")
  @Min(0) @Max(100)   public Integer getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(Integer discountPercent) {
    this.discountPercent = discountPercent;
  }

  public RentalRequest checkoutDate(String checkoutDate) {
    this.checkoutDate = checkoutDate;
    return this;
  }

  /**
   * Get checkoutDate
   * @return checkoutDate
   **/
  @Schema(required = true, description = "")
  public String getCheckoutDate() {
    return checkoutDate;
  }
  public void setCheckoutDate(String checkoutDate) {
    this.checkoutDate = checkoutDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RentalRequest rentalRequest = (RentalRequest) o;
    return Objects.equals(this.toolCode, rentalRequest.toolCode) &&
        Objects.equals(this.rentailDaysCount, rentalRequest.rentailDaysCount) &&
        Objects.equals(this.discountPercent, rentalRequest.discountPercent) &&
        Objects.equals(this.checkoutDate, rentalRequest.checkoutDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(toolCode, rentailDaysCount, discountPercent, checkoutDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RentalRequest {\n");
    
    sb.append("    toolCode: ").append(toIndentedString(toolCode)).append("\n");
    sb.append("    rentailDaysCount: ").append(toIndentedString(rentailDaysCount)).append("\n");
    sb.append("    discountPercent: ").append(toIndentedString(discountPercent)).append("\n");
    sb.append("    checkoutDate: ").append(toIndentedString(checkoutDate)).append("\n");
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
