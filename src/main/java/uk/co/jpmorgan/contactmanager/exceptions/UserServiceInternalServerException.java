package uk.co.jpmorgan.contactmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserServiceInternalServerException extends RuntimeException {
    public UserServiceInternalServerException(String message) {
        super(message);
    }
}
