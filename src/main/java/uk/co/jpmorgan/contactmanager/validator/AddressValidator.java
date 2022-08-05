package uk.co.jpmorgan.contactmanager.validator;

import uk.co.jpmorgan.contactmanager.dto.AddressDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static uk.co.jpmorgan.contactmanager.validator.ValidatorConstants.*;

public class AddressValidator
        implements ConstraintValidator<ValidateAddress, AddressDTO> {
    @Override
    public boolean isValid(AddressDTO address,
                           ConstraintValidatorContext context) {
        String houseNumber = address.getHouseNumber();
        String flatNumber = address.getFlatNumber();

        boolean noHouseOrFlat = (houseNumber == null || houseNumber.isEmpty()) && (flatNumber == null || flatNumber.isEmpty());
        if (noHouseOrFlat) {
            context.disableDefaultConstraintViolation(); // disable violation message
            context.buildConstraintViolationWithTemplate("Either house number or flat number is needed").addConstraintViolation();
            return false;
        }
        String countryCode = address.getCountryCode();
        if (countryCode != null && !ISO_COUNTRIES.contains(countryCode)) {
            context.disableDefaultConstraintViolation(); // disable violation message
            context.buildConstraintViolationWithTemplate(String.format(ERROR_COUNTRY_CODE, countryCode)).addConstraintViolation();
            return false;
        }
        String postCode = address.getPostCode();
        Pattern pattern = Pattern.compile(POSTCODE_REGEX);
        if (postCode!=null && !pattern.matcher(postCode).matches()) {
            context.disableDefaultConstraintViolation(); // disable violation message
            context.buildConstraintViolationWithTemplate(String.format(ERROR_POST_CODE, postCode)).addConstraintViolation();
            return false;
        }
        return true;
    }
}
