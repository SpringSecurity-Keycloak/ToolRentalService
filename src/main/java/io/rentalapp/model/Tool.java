package io.rentalapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rentalapp.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Tools available for rent
 */
@Schema(description = "Tools available for rent")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-07-06T15:29:49.511604531Z[GMT]")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tool  extends BaseEntity {

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("brand")
  private String brand = null;

  /*
  public Tool() {

  }

  public Tool(String code,String type, String brand) {
    this.code = code;
    this.type = type;
    this.brand = brand;
  }
  */


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

  public Tool type(String type) {
    this.type = type;
    return this;
  }

  /**
   * The type of tool.
   * @return type
   **/
  @Schema(required = true, description = "The type of tool.")
      @NotNull

    public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Tool brand(String brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Get brand
   * @return brand
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tool tool = (Tool) o;
    return Objects.equals(this.code, tool.code) &&
        Objects.equals(this.type, tool.type) &&
        Objects.equals(this.brand, tool.brand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, type, brand);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tool {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
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
