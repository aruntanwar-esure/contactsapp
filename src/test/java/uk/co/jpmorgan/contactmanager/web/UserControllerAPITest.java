package uk.co.jpmorgan.contactmanager.web;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import uk.co.jpmorgan.contactmanager.dto.UserListResponseDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;
import uk.co.jpmorgan.contactmanager.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerAPITest {
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

	private static UserRequestDTO userRequestDTO;

	@BeforeAll
	public static void setUp() {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones").address(addressDTO)
				.phoneNumbers(phoneDTOS).build();
	}

	@Test
	public void shouldCreateUser() throws Exception {
		UserResponseDTO userResponseDTO = UserResponseDTO.builder().userId("123").firstName("David").lastName("Jones")
				.address(userRequestDTO.getAddress()).phoneNumbers(userRequestDTO.getPhoneNumbers()).build();
		String userRequestJson = objectMapper.writeValueAsString(userRequestDTO);
		String userResponseJson = objectMapper.writeValueAsString(userResponseDTO);

		Mockito.when(userService.saveUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

		mockMvc.perform(MockMvcRequestBuilders.post(url + path).content(userRequestJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().json(userResponseJson))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void shouldReturnUser() throws Exception {
		UserResponseDTO userResponseDTO = UserResponseDTO.builder().userId("123").firstName("David").lastName("Jones")
				.address(userRequestDTO.getAddress()).phoneNumbers(userRequestDTO.getPhoneNumbers()).build();
		String userResponseJson = objectMapper.writeValueAsString(userResponseDTO);

		Mockito.when(userService.getUser(any(Long.class))).thenReturn(userResponseDTO);

		mockMvc.perform(MockMvcRequestBuilders.get(url + path + "/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(userResponseJson))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void shouldReturnUsers() throws Exception {
		UserResponseDTO userResponseDTO = UserResponseDTO.builder().userId("123").firstName("David").lastName("Jones")
				.address(userRequestDTO.getAddress()).phoneNumbers(userRequestDTO.getPhoneNumbers()).build();
		UserListResponseDTO userListResponseDTO = UserListResponseDTO.builder()
				.users(Collections.singletonList(userResponseDTO)).build();
		String userResponseJson = objectMapper.writeValueAsString(userListResponseDTO);

		Mockito.when(userService.getUsers(any(List.class))).thenReturn(userListResponseDTO);

		mockMvc.perform(MockMvcRequestBuilders.get(url + path + "?userIds=1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(userResponseJson))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}
}
