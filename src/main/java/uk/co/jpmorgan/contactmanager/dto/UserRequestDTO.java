package uk.co.jpmorgan.contactmanager.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "phoneNumber", "extensionNumber", "effectiveFrom", "effectiveTo" })
public class UserRequestDTO {
	@JsonProperty("firstName")
	@Size(max = 20)
	@NotBlank(message = "First name cannot be blank")
	private String firstName;

	@JsonProperty("middleName")
	@Size(max = 20)
	private String middleName;

	@JsonProperty("lastName")
	@Size(max = 20)
	@NotBlank(message = "Last name cannot be blank")
	private String lastName;

	@JsonProperty("address")
	@Valid
	@NotNull(message = "Address must not be null")
	private AddressDTO address;

	@JsonProperty("phoneNumbers")
	@Valid
	@NotNull(message = "Phone number(s) must not be null")
	private List<PhoneDTO> phoneNumbers;
}
