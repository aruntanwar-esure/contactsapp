package uk.co.jpmorgan.contactmanager.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"userId", "firstName", "lastName", "phoneNumber", "extensionNumber", "effectiveFrom", "effectiveTo"})
public class UserResponseDTO {
    @JsonProperty("id")
    private String userId;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("address")
    private AddressDTO address;

    @JsonProperty("phoneNumbers")
    private List<PhoneDTO> phones;

    @JsonProperty("created_At")
    private String created_At;
}
