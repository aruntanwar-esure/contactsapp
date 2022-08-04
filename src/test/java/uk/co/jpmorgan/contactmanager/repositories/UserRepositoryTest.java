package uk.co.jpmorgan.contactmanager.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jpmorgan.contactmanager.dto.AddressDTO;
import uk.co.jpmorgan.contactmanager.dto.EntityDTOConversionUtil;
import uk.co.jpmorgan.contactmanager.dto.PhoneDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.entity.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").county("Essex")
                .street("Morgan Way").buildingName("MorganMan").flatNumber("20").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").extensionNo("123").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").middleName("Well").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
        user.getPhoneNumbers().forEach(phone -> phone.setUser(user));
        user.getAddress().setUser(user);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreated_At()).isNotNull();
    }

    @Test
    public void shouldReturnUser() {
        AddressDTO addressDTO = AddressDTO.builder().buildingNumber("10")
                .houseNumber("110").city("London").postCode("IG88DL").county("Essex")
                .street("Morgan Way").buildingName("MorganMan").flatNumber("20").countryCode("GB").build();
        PhoneDTO phoneDTO = PhoneDTO.builder().internationalDialCode("+44").phoneNo("7889434000").extensionNo("123").build();
        List<PhoneDTO> phoneDTOS = Arrays.asList(phoneDTO);
        UserRequestDTO userRequestDTO = UserRequestDTO.builder().firstName("David").middleName("Well").lastName("Jones")
                .address(addressDTO).phones(phoneDTOS).build();
        User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
        user.getPhoneNumbers().forEach(phone -> phone.setUser(user));
        user.getAddress().setUser(user);

        User savedUser = userRepository.save(user);
        User found = userRepository.getById(savedUser.getId());

        assertThat(found.getFirstName())
                .isEqualTo(user.getFirstName());
    }
}
