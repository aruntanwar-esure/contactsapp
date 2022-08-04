package uk.co.jpmorgan.contactmanager.services;

import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;

public interface UserService {
    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO);
    public UserResponseDTO getUser(Long userId);
}
