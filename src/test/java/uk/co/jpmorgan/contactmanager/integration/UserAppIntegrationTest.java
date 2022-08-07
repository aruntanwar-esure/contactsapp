package uk.co.jpmorgan.contactmanager.integration;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jpmorgan.contactmanager.ContactApplication;
import uk.co.jpmorgan.contactmanager.dto.AddressDTO;
import uk.co.jpmorgan.contactmanager.dto.EntityDTOConversionUtil;
import uk.co.jpmorgan.contactmanager.dto.ErrorResponseDTO;
import uk.co.jpmorgan.contactmanager.dto.PhoneDTO;
import uk.co.jpmorgan.contactmanager.dto.UserListResponseDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;
import uk.co.jpmorgan.contactmanager.entity.User;
import uk.co.jpmorgan.contactmanager.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { ContactApplication.class })
public class UserAppIntegrationTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Value("${service.contactmanager.contact-service.url}")
	private String url;

	@Value("${service.contactmanager.contact-service.path}")
	private String path;

	private UserRequestDTO userRequestDTO;

	@BeforeEach
	public void setUp() {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones").address(addressDTO)
				.phoneNumbers(phoneDTOS).build();
	}

	@Test
	public void shouldSaveUser() {
		HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(userRequestDTO);
		ResponseEntity<UserResponseDTO> responseEntity = testRestTemplate.exchange(url + path, HttpMethod.POST,
				requestEntity, UserResponseDTO.class);
		Assertions.assertEquals("David", responseEntity.getBody().getFirstName());
		userRepository.deleteAll();
	}

	@Test
	public void shouldReturnUser() {
		User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
		user.getPhoneNumbers().forEach(phone -> phone.setUser(user));
		user.getAddress().setUser(user);
		userRepository.save(user);

		ResponseEntity<UserResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path + "/1",
				UserResponseDTO.class);
		Assertions.assertEquals("David", responseEntity.getBody().getFirstName());
	}

	@Test
	public void shouldReturnUsers() {
		User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
		user.getPhoneNumbers().forEach(phone -> phone.setUser(user));
		user.getAddress().setUser(user);
		userRepository.save(user);

		ResponseEntity<UserListResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path + "?=userIds=1",
				UserListResponseDTO.class);
		Assertions.assertEquals("David", responseEntity.getBody().getUsers().get(0).getFirstName());
	}

	@Test
	public void shouldThrowExceptionWhenArgumentsAreInvalid() {
		userRequestDTO.setAddress(null);
		HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(userRequestDTO);
		ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.exchange(url + path, HttpMethod.POST,
				requestEntity, ErrorResponseDTO.class);
		Assertions.assertEquals("Input fields not valid", responseEntity.getBody().getMessage());
		Assertions.assertEquals("Address must not be null", responseEntity.getBody().getErrorMap().get("address"));
	}

	@Test
	public void shouldThrowExceptionWhenUserAbsent() {
		ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path + "/1",
				ErrorResponseDTO.class);
		Assertions.assertEquals("No user found for id: 1", responseEntity.getBody().getMessage());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

	}

	@Test
	public void shouldThrowExceptionWhenUserIdInvalid() {
		ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path + "/0",
				ErrorResponseDTO.class);
		Assertions.assertEquals("User id should be at least 1", responseEntity.getBody().getMessage());
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void shouldThrowExceptionWhenUserIdTypeInvalid() {
		ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path + "/a",
				ErrorResponseDTO.class);
		Assertions.assertEquals("Input not valid", responseEntity.getBody().getMessage());
		Assertions.assertEquals("Incorrect type provided", responseEntity.getBody().getErrorMap().get("userId"));
	}

	@Test
	public void shouldThrowExceptionWhenUsersAreInvalid() {
		ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path + "?userIds=-1",
				ErrorResponseDTO.class);
		Assertions.assertEquals("User ids should not be empty and value of at least 1",
				responseEntity.getBody().getMessage());
	}

	@Test
	public void shouldThrowExceptionWhenRequestParamNotGiven() {
		ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path,
				ErrorResponseDTO.class);
		Assertions.assertEquals("Input not valid", responseEntity.getBody().getMessage());
		Assertions.assertEquals("Request param not provided", responseEntity.getBody().getErrorMap().get("userIds"));
	}

	@Test
	public void shouldThrowExceptionWhenRequestParamMisspelled() {
		ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(url + path + "?userIs=",
				ErrorResponseDTO.class);
		Assertions.assertEquals("Input not valid", responseEntity.getBody().getMessage());
		Assertions.assertEquals("Request param not provided", responseEntity.getBody().getErrorMap().get("userIds"));
	}
}
