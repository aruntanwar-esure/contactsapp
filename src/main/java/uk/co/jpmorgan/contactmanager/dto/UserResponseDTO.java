package uk.co.jpmorgan.contactmanager.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "userId", "firstName", "middleName", "lastName", "address", "phoneNumbers", "createdAt" })
public class UserResponseDTO {
	@JsonProperty("id")
	@ApiModelProperty(example = "123", position = 1)
	private Long userId;

	@JsonProperty("firstName")
	@ApiModelProperty(example = "Robin")
	private String firstName;

	@JsonProperty("middleName")
	@ApiModelProperty(example = "Stew")
	private String middleName;

	@JsonProperty("lastName")
	@ApiModelProperty(example = "Smith")
	private String lastName;

	@JsonProperty("address")
	private AddressDTO address;

	@JsonProperty("phoneNumbers")
	private List<PhoneDTO> phoneNumbers;

	@ApiModelProperty(example = "2022-08-07")
	private String createdAt;

	/**
	 * String conversion from LocalDate
	 * 
	 * @param effectiveFrom
	 */
	@JsonProperty("createdAt")
	public void setCreatedAt(Object createdAt) {
		if (createdAt instanceof String) {
			this.createdAt = (String) createdAt;
		} else {
			this.createdAt = Optional.ofNullable(createdAt).map(v -> convertToStringDate(v)).orElse(null);
		}
	}

	/**
	 * Converts to String date when deserializing from ContactPreferenceT to this
	 * object
	 * 
	 * @param date - date coming as LinkedHashMap from default implementation
	 * @return
	 */
	private String convertToStringDate(Object date) {
		LinkedHashMap dateTokens = (LinkedHashMap) date;
		StringJoiner dateJoiner = new StringJoiner("-");
		dateJoiner.add(dateTokens.get("year").toString());
		dateJoiner.add(String.format("%02d", dateTokens.get("monthValue")));
		dateJoiner.add(String.format("%02d", dateTokens.get("dayOfMonth")));
		return dateJoiner.toString();
	}
}
