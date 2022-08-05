package uk.co.jpmorgan.contactmanager.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.jpmorgan.contactmanager.entity.User;
import uk.co.jpmorgan.contactmanager.exceptions.UserServiceInternalServerException;
import uk.co.jpmorgan.contactmanager.services.UserServiceImpl;

public class EntityDTOConversionUtil {
	private EntityDTOConversionUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private static ObjectMapper mapper = new ObjectMapper();

	public static UserResponseDTO convertEntityToDTO(User entity) {
		try {
			return mapper.convertValue(entity, new TypeReference<UserResponseDTO>() {
			});
		} catch (Exception e) {
			LOGGER.error("Error in converting user to dto", e);
			throw new UserServiceInternalServerException("Data processing failed, please contact helpdesk");
		}
	}

	public static User covertDTOToEntity(UserRequestDTO entity) {
		try {
			return mapper.convertValue(entity, new TypeReference<User>() {
			});
		} catch (IllegalArgumentException e) {
			LOGGER.error("Error in converting dto to user", e);
			throw new UserServiceInternalServerException("Data processing failed, please contact helpdesk");
		}
	}
}
