package uk.co.jpmorgan.contactmanager.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jpmorgan.contactmanager.dto.AddressDTO;

import javax.validation.ConstraintValidatorContext;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AddressValidatorTest {
    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;
    @Mock
    private ConstraintValidatorContext context;
    private AddressValidator addressValidator = new AddressValidator();

    @Captor
    ArgumentCaptor<String> errorMessage;

    @Test
    public void shouldReturnFlatOrHouseErrorWhenBothNull() {
        AddressDTO addressDTO = AddressDTO.builder().build();
        Mockito.when(context.buildConstraintViolationWithTemplate(any(String.class))).thenReturn(constraintViolationBuilder);

        addressValidator.isValid(addressDTO, context);
        Mockito.verify(context).buildConstraintViolationWithTemplate(errorMessage.capture());
        Assertions.assertEquals("Either house number or flat number is needed", errorMessage.getValue());
    }

    @Test
    public void shouldReturnFlatOrHouseErrorWhenBothEmpty() {
        AddressDTO addressDTO = AddressDTO.builder().houseNumber("").flatNumber("").build();
        Mockito.when(context.buildConstraintViolationWithTemplate(any(String.class))).thenReturn(constraintViolationBuilder);

        addressValidator.isValid(addressDTO, context);
        Mockito.verify(context).buildConstraintViolationWithTemplate(errorMessage.capture());
        Assertions.assertEquals("Either house number or flat number is needed", errorMessage.getValue());
    }

    @Test
    public void shouldReturnCountryCodeErrorWhenNotValid() {
        AddressDTO addressDTO = AddressDTO.builder().houseNumber("110").countryCode("BGH").build();
        Mockito.when(context.buildConstraintViolationWithTemplate(any(String.class))).thenReturn(constraintViolationBuilder);

        addressValidator.isValid(addressDTO, context);
        Mockito.verify(context).buildConstraintViolationWithTemplate(errorMessage.capture());
        Assertions.assertEquals("Country code BGH is not valid", errorMessage.getValue());
    }

    @Test
    public void shouldReturnPostCodeErrorWhenNotValid() {
        AddressDTO addressDTO = AddressDTO.builder().houseNumber("110").postCode("IG88").build();
        Mockito.when(context.buildConstraintViolationWithTemplate(any(String.class))).thenReturn(constraintViolationBuilder);

        addressValidator.isValid(addressDTO, context);
        Mockito.verify(context).buildConstraintViolationWithTemplate(errorMessage.capture());
        Assertions.assertEquals("Post code IG88 is not valid", errorMessage.getValue());
    }

    @Test
    public void shouldReturnNoErrorWhenValuesValid() {
        AddressDTO addressDTO = AddressDTO.builder().houseNumber("110").countryCode("GB").postCode("IG88DL").build();
        boolean isValid = addressValidator.isValid(addressDTO, context);
        Assertions.assertEquals(true, isValid);
    }
}
