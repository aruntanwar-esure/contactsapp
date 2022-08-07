package uk.co.jpmorgan.contactmanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.co.jpmorgan.contactmanager.validator.ValidateAddress;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "houseNumber", "flatNumber", "buildingNumber", "buildingName", "street", "county", "postCode",
		"city", "countryCode" })
@ValidateAddress
public class AddressDTO {
	@JsonProperty("houseNumber")
	@Size(max = 5)
	@ApiModelProperty(example = "2A", value = "Either houseNumber or flatNumber required, can be alphanumeric")
	private String houseNumber;

	@JsonProperty("flatNumber")
	@Size(max = 5)
	@ApiModelProperty(example = "3C", value = "Either houseNumber or flatNumber required, can be alphanumeric")
	private String flatNumber;

	@JsonProperty("buildingNumber")
	@Size(max = 5)
	@ApiModelProperty(example = "10", value = "BuildingNumber can be alphanumeric")
	private String buildingNumber;

	@JsonProperty("buildingName")
	@Size(max = 50)
	@ApiModelProperty(example = "Essex House")
	private String buildingName;

	@JsonProperty("street")
	@NotBlank(message = "Street cannot be blank")
	@ApiModelProperty(example = "Gibson Street", required = true)
	@Size(max = 50)
	private String street;

	@JsonProperty("county")
	@Size(max = 20)
	@ApiModelProperty(example = "Essex")
	private String county;

	@JsonProperty("postCode")
	@NotNull(message = "Post code cannot be null")
	@ApiModelProperty(example = "IG87AB", required = true)
	private String postCode;

	@JsonProperty("city")
	@NotNull(message = "City cannot be null")
	@Size(max = 20)
	@ApiModelProperty(example = "London", required = true)
	private String city;

	@JsonProperty("countryCode")
	@NotNull(message = "Country code cannot be null")
	@Size(max = 2, min = 2)
	@ApiModelProperty(example = "GB", required = true)
	private String countryCode;
}
