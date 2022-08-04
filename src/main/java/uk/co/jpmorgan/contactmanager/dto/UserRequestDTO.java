package uk.co.jpmorgan.contactmanager.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"firstName", "lastName", "phoneNumber", "extensionNumber", "effectiveFrom", "effectiveTo"})
public class UserRequestDTO {
    @JsonProperty("firstName")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @JsonProperty("address")
    @Valid
    @NotNull(message = "Address must not be null")
    private AddressDTO address;

    @JsonProperty("phoneNumbers")
    @Valid
    @NotNull(message = "Phone number(s) must not be null")
    private List<PhoneDTO> phones;
}
