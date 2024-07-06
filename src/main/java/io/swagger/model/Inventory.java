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
 * Inventory
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T12:46:17.388586925Z[GMT]")


public class Inventory   {
  @JsonProperty("tool_code")
  private String toolCode = null;

  @JsonProperty("max_available")
  private BigDecimal maxAvailable = null;

  @JsonProperty("current_count")
  private BigDecimal currentCount = null;

  public Inventory toolCode(String toolCode) {
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

  public Inventory maxAvailable(BigDecimal maxAvailable) {
    this.maxAvailable = maxAvailable;
    return this;
  }

  /**
   * Get maxAvailable
   * @return maxAvailable
   **/
  @Schema(description = "")
      @NotNull

    @Valid
    public BigDecimal getMaxAvailable() {
    return maxAvailable;
  }

  public void setMaxAvailable(BigDecimal maxAvailable) {
    this.maxAvailable = maxAvailable;
  }

  public Inventory currentCount(BigDecimal currentCount) {
    this.currentCount = currentCount;
    return this;
  }

  /**
   * Get currentCount
   * @return currentCount
   **/
  @Schema(description = "")
      @NotNull

    @Valid
    public BigDecimal getCurrentCount() {
    return currentCount;
  }

  public void setCurrentCount(BigDecimal currentCount) {
    this.currentCount = currentCount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Inventory inventory = (Inventory) o;
    return Objects.equals(this.toolCode, inventory.toolCode) &&
        Objects.equals(this.maxAvailable, inventory.maxAvailable) &&
        Objects.equals(this.currentCount, inventory.currentCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(toolCode, maxAvailable, currentCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Inventory {\n");
    
    sb.append("    toolCode: ").append(toIndentedString(toolCode)).append("\n");
    sb.append("    maxAvailable: ").append(toIndentedString(maxAvailable)).append("\n");
    sb.append("    currentCount: ").append(toIndentedString(currentCount)).append("\n");
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
