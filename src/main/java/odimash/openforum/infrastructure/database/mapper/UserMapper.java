package odimash.openforum.infrastructure.database.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.infrastructure.database.dto.UserDTO;

@Component
public class UserMapper {

	@Autowired
	UserRepository userRepository;

	public UserDTO mapToDTO(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
	}

	public User mapToEntity(UserDTO userDTO) {
		return new User(
			userDTO.getId(),
			userDTO.getUsername(),
			userDTO.getEmail(),
			userDTO.getPassword(),
			userDTO.getId() == null ?
				null : userRepository.findRolesById(userDTO.getId())
		);
	}

}
