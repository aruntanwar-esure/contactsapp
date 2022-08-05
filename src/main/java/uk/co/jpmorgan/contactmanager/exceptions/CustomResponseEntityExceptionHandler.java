package uk.co.jpmorgan.contactmanager.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonParseException;

import uk.co.jpmorgan.contactmanager.dto.ErrorResponseDTO;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final Object handleUserIdException(UserIdException exception) {
		LOGGER.error("Invalid user id: {}", exception.getMessage());
		return ErrorResponseDTO.builder().status(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(exception.getMessage()).build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public final Object handleUserNotFoundException(UserNotFoundException exception) {
		LOGGER.error("No user found: {}", exception.getMessage());
		return ErrorResponseDTO.builder().status(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(exception.getMessage()).build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public final Object handleInternalServerException(UserServiceInternalServerException exception) {
		LOGGER.error("Internal Server exception: {}", exception.getMessage());
		return ErrorResponseDTO.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.message(exception.getMessage()).build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final Object handleMethodArgumentsNotValid(MethodArgumentNotValidException exception) {
		LOGGER.error("Error in validation occurred: MethodArgumentNotValidException", exception);

		Map<String, String> errorMap = new HashMap<>();
		exception.getBindingResult().getFieldErrors()
				.forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

		return ErrorResponseDTO.builder().status(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message("Input fields not valid").errorMap(errorMap).build();
	}

	@ExceptionHandler(JsonParseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object jsonParsingExceptionHandler(JsonParseException exception) {
		LOGGER.error("Error in json format: {}", exception.getMessage());
		return ErrorResponseDTO.builder().status(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message("Json is not formatted properly").build();
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final Object handleMissingRequestParameterException(MissingServletRequestParameterException exception) {
		LOGGER.error("Error in validation occurred: MissingServletRequestParameterException", exception);

		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(exception.getParameterName(), "Request param not provided");

		return ErrorResponseDTO.builder().status(HttpStatus.BAD_REQUEST.getReasonPhrase()).message("Input not valid")
				.errorMap(errorMap).build();
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final Object handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		LOGGER.error("Error in validation occurred: MethodArgumentTypeMismatchException", exception);

		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(exception.getParameter().getParameterName(), "Incorrect type provided");

		return ErrorResponseDTO.builder().status(HttpStatus.BAD_REQUEST.getReasonPhrase()).message("Input not valid")
				.errorMap(errorMap).build();
	}
}
