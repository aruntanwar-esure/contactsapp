package uk.co.jpmorgan.contactmanager.validator;

import uk.co.jpmorgan.contactmanager.dto.PhoneDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static uk.co.jpmorgan.contactmanager.validator.ValidatorConstants.*;

public class PhoneValidator
        implements ConstraintValidator<ValidatePhone, PhoneDTO> {
    @Override
    public boolean isValid(PhoneDTO phone,
                           ConstraintValidatorContext context) {
        String internationDialCode = phone.getInternationalDialCode();
        Pattern pattern = Pattern.compile(INTERNATIONAL_CODE_REGEX);
        if (internationDialCode != null && !pattern.matcher(internationDialCode).matches()) {
            context.disableDefaultConstraintViolation(); // disable violation message
            context.buildConstraintViolationWithTemplate(String.format(ERROR_INTERNATIONAL_DIAL_CODE, internationDialCode)).addConstraintViolation();
            return false;
        }

        String phoneNumber = phone.getPhoneNo();
        pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        if (phoneNumber != null && !pattern.matcher(phoneNumber).matches()) {
            context.disableDefaultConstraintViolation(); // disable violation message
            context.buildConstraintViolationWithTemplate(String.format(ERROR_PHONE_NUMBER, phoneNumber)).addConstraintViolation();
            return false;
        }
        return true;
    }
}
