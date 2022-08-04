package uk.co.jpmorgan.contactmanager.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;
import uk.co.jpmorgan.contactmanager.exceptions.UserIdException;
import uk.co.jpmorgan.contactmanager.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/jpmorgan/contactmanager/v1")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        LOGGER.info("Save user call to the service started");

        UserResponseDTO userResponseDTO = userService.saveUser(userRequestDTO);

        LOGGER.info("Save user call to the service ended");
        return new ResponseEntity<UserResponseDTO>(userResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> retrieveUser(@PathVariable Long userId) {
        LOGGER.info("Get user call to the service started");
        if (userId < 1) {
            LOGGER.error(String.format("Error in supplied user id: %s", userId));
            throw new UserIdException("User id should be at least 1");
        }
        UserResponseDTO userResponseDTO = userService.getUser(userId);

        LOGGER.info("Get user call to the service ended");
        return new ResponseEntity<UserResponseDTO>(userResponseDTO, HttpStatus.OK);
    }
}
