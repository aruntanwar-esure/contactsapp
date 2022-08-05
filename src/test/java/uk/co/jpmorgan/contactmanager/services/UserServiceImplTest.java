package uk.co.jpmorgan.contactmanager.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jpmorgan.contactmanager.dto.AddressDTO;
import uk.co.jpmorgan.contactmanager.dto.EntityDTOConversionUtil;
import uk.co.jpmorgan.contactmanager.dto.PhoneDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.entity.User;
import uk.co.jpmorgan.contactmanager.exceptions.UserNotFoundException;
import uk.co.jpmorgan.contactmanager.exceptions.UserServiceInternalServerException;
import uk.co.jpmorgan.contactmanager.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

	@TestConfiguration
	static class UserServiceImpTestContextConfiguration {
		@Bean
		public UserService employeeService() {
			return new UserServiceImpl();
		}
	}

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@Test
	public void shouldSaveUser() {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
				.address(addressDTO).phoneNumbers(phoneDTOS).build();
		User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
		user.setId(1L);

		when(userRepository.save(any(User.class))).thenReturn(user);
		Assertions.assertEquals("1", userService.saveUser(userRequestDTO).getUserId());
	}

	@Test
	public void shouldRetrieveUser() {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
				.address(addressDTO).phoneNumbers(phoneDTOS).build();
		User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
		user.setId(1L);

		when(userRepository.getById(any(Long.class))).thenReturn(user);
		Assertions.assertEquals("1", userService.getUser(1L).getUserId());
	}

	@Test
	public void shouldRetrieveUsers() {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
				.address(addressDTO).phoneNumbers(phoneDTOS).build();
		User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
		user.setId(1L);

		List<User> users = Collections.singletonList(user);
		when(userRepository.findByIdIn(any(List.class))).thenReturn(users);
		Assertions.assertEquals("1", userService.getUsers(Collections.singletonList(1L)).getUsers().get(0).getUserId());
	}

	@Test
	public void shouldThrowExceptionForUserIdAbsent() {
		when(userRepository.getById(any(Long.class))).thenReturn(null);
		UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
				() -> userService.getUser(1L), "UserNotFoundException was expected");
		Assertions.assertEquals("No user found for id: 1", exception.getMessage());
	}

	@Test
	public void shouldThrowExceptionForUserIdsAbsent() {
		when(userRepository.findByIdIn(any(List.class))).thenReturn(Collections.EMPTY_LIST);
		UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
				() -> userService.getUsers(Collections.singletonList(1L)), "UserNotFoundException was expected");
		Assertions.assertEquals("No users found for ids: [1]", exception.getMessage());
	}
	
	@Test
	public void shouldThrowExceptionForUserIdsAbsent1() {
		when(userRepository.findByIdIn(any(List.class))).thenReturn(null);
		UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
				() -> userService.getUsers(Collections.singletonList(1L)), "UserNotFoundException was expected");
		Assertions.assertEquals("No users found for ids: [1]", exception.getMessage());
	}

	@Test
	public void shouldThrowExceptionOnDBErrorInSave() {
		AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10").houseNumber("110").city("London")
				.postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
		PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNumber(7889434000L).build();
		List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
		UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
				.address(addressDTO).phoneNumbers(phoneDTOS).build();
		User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
		user.setId(1L);

		when(userRepository.save(any(User.class))).thenThrow(IllegalStateException.class);

		UserServiceInternalServerException exception = Assertions.assertThrows(UserServiceInternalServerException.class,
				() -> userService.saveUser(userRequestDTO), "UserServiceInternalServerException was expected");
		Assertions.assertEquals("User could not be saved in store, please try again", exception.getMessage());
	}

	@Test
	public void shouldThrowExceptionOnDBErrorInUserGet() {
		when(userRepository.getById(any(Long.class))).thenThrow(IllegalStateException.class);
		UserServiceInternalServerException exception = Assertions.assertThrows(UserServiceInternalServerException.class,
				() -> userService.getUser(1L), "UserServiceInternalServerException was expected");
		Assertions.assertEquals("User could not be retrieved from store, please try again", exception.getMessage());
	}

	@Test
	public void shouldThrowExceptionOnDBErrorInUsersGet() {
		when(userRepository.findByIdIn(any(List.class))).thenThrow(IllegalStateException.class);
		UserServiceInternalServerException exception = Assertions.assertThrows(UserServiceInternalServerException.class,
				() -> userService.getUsers(Collections.singletonList(1L)),
				"UserServiceInternalServerException was expected");
		Assertions.assertEquals("Users could not be retrieved from store, please try again", exception.getMessage());
	}
}