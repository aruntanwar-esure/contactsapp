package uk.co.jpmorgan.contactmanager.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = {PhoneValidator.class})
public @interface ValidatePhone {
    String message() default "{uk.co.jpmorgan.contact.validation.ValidPhone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
