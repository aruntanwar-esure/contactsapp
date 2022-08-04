package uk.co.jpmorgan.contactmanager.integration;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jpmorgan.contactmanager.ContactApplication;
import uk.co.jpmorgan.contactmanager.dto.*;
import uk.co.jpmorgan.contactmanager.entity.User;
import uk.co.jpmorgan.contactmanager.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ContactApplication.class})
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
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
    }

    @Test
    public void shouldSaveUser() {
        HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(userRequestDTO);
        ResponseEntity<UserResponseDTO> responseEntity = testRestTemplate.exchange(
                url + path, HttpMethod.POST, requestEntity, UserResponseDTO.class);
        Assertions.assertEquals("David", responseEntity.getBody().getFirstName());
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnUser() {
        User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
        user.getPhoneNumbers().forEach(phone -> phone.setUser(user));
        user.getAddress().setUser(user);
        userRepository.save(user);

        HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(userRequestDTO);
        ResponseEntity<UserResponseDTO> responseEntity = testRestTemplate.getForEntity(
                url + path + "/1", UserResponseDTO.class);
        Assertions.assertEquals("David", responseEntity.getBody().getFirstName());
    }

    @Test
    public void shouldThrowExceptionWhenUserAbsent() {
        HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(userRequestDTO);
        ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(
                url + path + "/1", ErrorResponseDTO.class);
        Assertions.assertEquals("No user found for id: 1", responseEntity.getBody().getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserIdInvalid() {
        HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(userRequestDTO);
        ResponseEntity<ErrorResponseDTO> responseEntity = testRestTemplate.getForEntity(
                url + path + "/0", ErrorResponseDTO.class);
        Assertions.assertEquals("User id should be at least 1", responseEntity.getBody().getMessage());
    }
}
