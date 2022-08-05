package uk.co.jpmorgan.contactmanager.web;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.jpmorgan.contactmanager.dto.AddressDTO;
import uk.co.jpmorgan.contactmanager.dto.PhoneDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerValidationTest {
	@MockBean
	private UserService userService;

	@Autowired
	UserController userController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${service.contactmanager.contact-service.url}")
	private String url;

	@Value("${service.contactmanager.contact-service.path}")
	private String path;

	@Test
	public void shouldReturnErrorsForMissingUserInfo() throws Exception {
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().build();
		String userJson = objectMapper.writeValueAsString(userRequestDTO);

		mockMvc.perform(
				MockMvcRequestBuilders.post(url + path).content(userJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Is.is("Bad Request")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Input fields not valid")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.firstName", Is.is("First name cannot be blank")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.lastName", Is.is("Last name cannot be blank")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.address", Is.is("Address must not be null")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.phoneNumbers",
						Is.is("Phone number(s) must not be null")))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void shouldReturnErrorsForMissingAddressInfo() throws Exception {
		AddressDTO addressDTO = AddressDTO.builder().build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
				.address(addressDTO).phoneNumbers(phoneDTOS).build();
		String userJson = objectMapper.writeValueAsString(userRequestDTO);

		mockMvc.perform(
				MockMvcRequestBuilders.post(url + path).content(userJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Is.is("Bad Request")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Input fields not valid")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.address",
						Is.is("Either house number or flat number is needed")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.['address.city']", Is.is("City cannot be null")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.['address.countryCode']",
						Is.is("Country code cannot be null")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.['address.city']", Is.is("City cannot be null")))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.errors.['address.street']", Is.is("Street cannot be blank")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.['address.postCode']",
						Is.is("Post code cannot be null")))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void shouldReturnErrorsForMissingPhoneInfo() throws Exception {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
				.address(addressDTO).phoneNumbers(phoneDTOS).build();
		String userJson = objectMapper.writeValueAsString(userRequestDTO);

		mockMvc.perform(
				MockMvcRequestBuilders.post(url + path).content(userJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Is.is("Bad Request")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Input fields not valid")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.['phoneNumbers[0].internationalDialCode']",
						Is.is("International dialing code cannot be blank")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors.['phoneNumbers[0].phoneNumber']",
						Is.is("Phone number cannot be null")))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void shouldReturnNoErrorsWhenAllInfoPresent() throws Exception {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
				.address(addressDTO).phoneNumbers(phoneDTOS).build();
		String userJson = objectMapper.writeValueAsString(userRequestDTO);

		mockMvc.perform(
				MockMvcRequestBuilders.post(url + path).content(userJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}
}
