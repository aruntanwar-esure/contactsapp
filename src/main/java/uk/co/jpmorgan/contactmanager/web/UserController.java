package uk.co.jpmorgan.contactmanager.web;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.co.jpmorgan.contactmanager.dto.UserListResponseDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;
import uk.co.jpmorgan.contactmanager.exceptions.UserIdException;
import uk.co.jpmorgan.contactmanager.services.UserService;

@RestController
@RequestMapping("/jpmorgan/contactmanager/v1")
@ApiOperation("Products API")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Saves a user object to store")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created status when user created successfully"),
			@ApiResponse(code = 400, message = "Bad request status when invalid values provided"),
			@ApiResponse(code = 500, message = "Internal Server Error when unable to store user due to server error") })
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
		LOGGER.info("Save user call to the service started");

		UserResponseDTO userResponseDTO = userService.saveUser(userRequestDTO);

		LOGGER.info("Save user call to the service ended");
		return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Gets a user from the store")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful status when user returned successfully"),
			@ApiResponse(code = 400, message = "Bad request status when invalid user id provided"),
			@ApiResponse(code = 404, message = "Not found status when no user found"),
			@ApiResponse(code = 500, message = "Internal Server Error when unable to retrieve user due to server error") })
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponseDTO> retrieveUser(@PathVariable Long userId) {
		LOGGER.info("Get user call to the service started");
		if (userId < 1) {
			LOGGER.error("Error in supplied user id: {}", userId);
			throw new UserIdException("User id should be at least 1");
		}
		UserResponseDTO userResponseDTO = userService.getUser(userId);

		LOGGER.info("Get user call to the service ended");
		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Gets users from the store")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful status when users returned successfully"),
			@ApiResponse(code = 400, message = "Bad request status when invalid user ids provided"),
			@ApiResponse(code = 404, message = "Not found status when no users found"),
			@ApiResponse(code = 500, message = "Internal Server Error when unable to retrieve users due to server error") })
	@GetMapping("/users")
	public ResponseEntity<UserListResponseDTO> retrieveUsers(@RequestParam List<Long> userIds) {
		LOGGER.info("Get user call to the service started");
		boolean notValid = userIds.stream().anyMatch(userID -> userID < 1);
		if (notValid || userIds.isEmpty()) {
			LOGGER.error("User ids should not be empty and value of at least 1. Given ids {}", userIds);
			throw new UserIdException("User ids should not be empty and value of at least 1");
		}
		UserListResponseDTO userResponseDTO = userService.getUsers(userIds);

		LOGGER.info("Get user call to the service ended");
		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
	}
}
