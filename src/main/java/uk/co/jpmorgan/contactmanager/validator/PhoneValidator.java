package uk.co.jpmorgan.contactmanager.validator;

import static uk.co.jpmorgan.contactmanager.validator.ValidatorConstants.ERROR_INTERNATIONAL_DIAL_CODE;
import static uk.co.jpmorgan.contactmanager.validator.ValidatorConstants.ERROR_PHONE_NUMBER;
import static uk.co.jpmorgan.contactmanager.validator.ValidatorConstants.INTERNATIONAL_CODE_REGEX;
import static uk.co.jpmorgan.contactmanager.validator.ValidatorConstants.PHONE_NUMBER_REGEX;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.co.jpmorgan.contactmanager.dto.PhoneDTO;

public class PhoneValidator implements ConstraintValidator<ValidatePhone, PhoneDTO> {
	@Override
	public boolean isValid(PhoneDTO phone, ConstraintValidatorContext context) {
		String internationDialCode = phone.getInternationalDialCode();
		Pattern pattern = Pattern.compile(INTERNATIONAL_CODE_REGEX);
		if (internationDialCode != null && !pattern.matcher(internationDialCode).matches()) {
			context.disableDefaultConstraintViolation(); // disable violation message
			context.buildConstraintViolationWithTemplate(
					String.format(ERROR_INTERNATIONAL_DIAL_CODE, internationDialCode)).addConstraintViolation();
			return false;
		}

		Long phoneNumber = phone.getPhoneNumber();
		pattern = Pattern.compile(PHONE_NUMBER_REGEX);
		if (phoneNumber != null && !pattern.matcher(String.valueOf(phoneNumber)).matches()) {
			context.disableDefaultConstraintViolation(); // disable violation message
			context.buildConstraintViolationWithTemplate(String.format(ERROR_PHONE_NUMBER, phoneNumber))
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
