package uk.co.jpmorgan.contactmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.co.jpmorgan.contactmanager.dto.AddressDTO;
import uk.co.jpmorgan.contactmanager.dto.PhoneDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;
import uk.co.jpmorgan.contactmanager.services.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

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

    @Test
    public void shouldCreateUser() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().userId("123").firstName("David").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        String userRequestJson = objectMapper.writeValueAsString(userRequestDTO);
        String userResponseJson = objectMapper.writeValueAsString(userResponseDTO);

        Mockito.when(userService.saveUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(url + path)
                        .content(userRequestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(userResponseJson))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void shouldReturnUser() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").street("Morgan Way").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().userId("123").firstName("David").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        String userResponseJson = objectMapper.writeValueAsString(userResponseDTO);

        Mockito.when(userService.getUser(any(Long.class))).thenReturn(userResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(url + path + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userResponseJson))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
