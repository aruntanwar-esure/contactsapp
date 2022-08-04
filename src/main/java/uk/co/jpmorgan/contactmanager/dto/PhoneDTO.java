package uk.co.jpmorgan.contactmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import uk.co.jpmorgan.contactmanager.validator.ValidatePhone;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"internationalDialCode", "phoneType", "phoneNumber", "extensionNumber"})
@ValidatePhone
public class PhoneDTO {
    @JsonProperty("internationalDialCode")
    @NotBlank(message = "International dialing code cannot be blank")
    private String internationalDialCode;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNo;

    @JsonProperty("extensionNumber")
    private String extensionNo;
}
