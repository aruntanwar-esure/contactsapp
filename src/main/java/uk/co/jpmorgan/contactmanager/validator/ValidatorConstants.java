package uk.co.jpmorgan.contactmanager.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface ValidatorConstants {
    public static final String INTERNATIONAL_CODE_REGEX = "^(\\+\\d{1,3}( )?)?$";
    public static final String PHONE_NUMBER_REGEX = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
    public static final String POSTCODE_REGEX = "^[A-Z]{1,2}[0-9R][0-9A-Z]?[0-9][ABD-HJLNP-UW-Z]{2}$";

    public static final List<String> ISO_COUNTRIES = Arrays.asList(Locale.getISOCountries());

    public static final String ERROR_INTERNATIONAL_DIAL_CODE = "International dial code %s is not valid";
    public static final String ERROR_PHONE_NUMBER = "Phone number %s is not valid";
    public static final String ERROR_COUNTRY_CODE = "Country code %s is not valid";
    public static final String ERROR_POST_CODE = "Post code %s is not valid";
}
