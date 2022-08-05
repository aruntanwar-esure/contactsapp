package uk.co.jpmorgan.contactmanager.services;

import java.util.List;

import uk.co.jpmorgan.contactmanager.dto.UserListResponseDTO;
import uk.co.jpmorgan.contactmanager.dto.UserRequestDTO;
import uk.co.jpmorgan.contactmanager.dto.UserResponseDTO;

public interface UserService {
	public UserResponseDTO saveUser(UserRequestDTO userRequestDTO);

	public UserResponseDTO getUser(Long userId);

	public UserListResponseDTO getUsers(List<Long> userIds);
}
