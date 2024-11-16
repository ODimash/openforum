package odimash.openforum.infrastructure.database.mapper;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.RoleRepository;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.infrastructure.database.dto.UserDTO;

@Component
public class UserMapper {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public UserDTO mapToDTO(User user) {
		return user == null ? null : new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
	}

	public User mapToEntity(UserDTO userDTO) {
		return new User(
			userDTO.getId(),
			userDTO.getUsername(),
			userDTO.getEmail(),
			userDTO.getPassword(),
			userDTO.getId() == null ?
				Set.of(roleRepository.findById(1L).get()) : userRepository.findRolesById(userDTO.getId())
		);
	}

}
