package uk.co.jpmorgan.contactmanager.services;

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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);
        Assertions.assertEquals("1", userService.saveUser(userRequestDTO).getUserId());
    }

    @Test
    public void shouldRetrieveUser() {
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
        user.setId(1L);

        when(userRepository.getById(any(Long.class))).thenReturn(user);
        Assertions.assertEquals("1", userService.getUser(1L).getUserId());
    }

    @Test
    public void shouldThrowExceptionForUserIdAbsent() {
        when(userRepository.getById(any(Long.class))).thenReturn(null);
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUser(1L), "UserNotFoundException was expected");
        Assertions.assertEquals("No user found for id: 1", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionOnDBErrorInSave() {
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenThrow(IllegalStateException.class);

        UserServiceInternalServerException exception = Assertions.assertThrows(UserServiceInternalServerException.class,
                () -> userService.saveUser(userRequestDTO), "UserServiceInternalServerException was expected");
        Assertions.assertEquals("User could not be saved in store, please try again", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionOnDBErrorInGet() {
        when(userRepository.getById(any(Long.class))).thenThrow(IllegalStateException.class);
        UserServiceInternalServerException exception = Assertions.assertThrows(UserServiceInternalServerException.class,
                () -> userService.getUser(1L), "UserServiceInternalServerException was expected");
        Assertions.assertEquals("User could not be retrieved from store, please try again", exception.getMessage());
    }
}