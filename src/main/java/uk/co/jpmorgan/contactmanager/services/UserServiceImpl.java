package uk.co.jpmorgan.contactmanager.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.jpmorgan.contactmanager.dto.EntityDTOConversionUtil;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;
import uk.co.jpmorgan.contactmanager.entity.User;
import uk.co.jpmorgan.contactmanager.exceptions.UserNotFoundException;
import uk.co.jpmorgan.contactmanager.exceptions.UserServiceInternalServerException;
import uk.co.jpmorgan.contactmanager.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        LOGGER.info("Saving to the database started");
        User user = EntityDTOConversionUtil.covertDTOToEntity(userRequestDTO);
        user.getPhoneNumbers().forEach(phone -> phone.setUser(user));
        user.getAddress().setUser(user);

        User savedUser = null;
        try {
            savedUser = userRepository.save(user);
            LOGGER.info("Successfully saved the user to the database");
        } catch (Exception e) {
            LOGGER.error("Error in saving user information to the database", e);
            throw new UserServiceInternalServerException("User could not be saved in store, please try again");
        }
        UserResponseDTO userResponseDTO = EntityDTOConversionUtil.convertEntityToDTO(savedUser);
        LOGGER.info("Saving to the database ended, returning response");
        return userResponseDTO;
    }

    @Override
    public UserResponseDTO getUser(Long userId) {
        LOGGER.info("Getting the user info from database");
        User user = null;
        try {
            user = userRepository.getById(userId);
            LOGGER.info("Successfully retrieved the user info from database");
        } catch (Exception dbException) {
            LOGGER.error("Error in retrieving user information from the database", dbException);
            throw new UserServiceInternalServerException("User could not be retrieved from store, please try again");
        }
        if (user == null) {
            LOGGER.error(String.format("No user found for id: %s", userId));
            throw new UserNotFoundException(String.format("No user found for id: %s", userId));
        }
        UserResponseDTO userResponseDTO = EntityDTOConversionUtil.convertEntityToDTO(user);
        LOGGER.info("Retrieving from the database ended, returning response");
        return userResponseDTO;
    }
}
