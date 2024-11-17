package odimash.openforum.infrastructure.database.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.entity.User;
import odimash.openforum.infrastructure.database.dto.RoleDTO;
import odimash.openforum.infrastructure.database.dto.UserDTO;

@Component
public class UserMapper {
	@Autowired
	private RoleMapper roleMapper;

	public UserDTO mapToDTO(User user) {
		Set<RoleDTO> rolesDTO = user.getRoles() != null
				? user.getRoles().stream().map(roleMapper::mapToDTO).collect(Collectors.toSet())
				: null;

		return user == null ? null
				: new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), rolesDTO);
	}

	public User mapToEntity(UserDTO userDTO) {
		Set<Role> convertedRoles = userDTO.getRoles() != null
				? userDTO.getRoles().stream().map(roleMapper::mapToEntity).collect(Collectors.toSet())
				: null;

		return new User(
				userDTO.getId(),
				userDTO.getUsername(),
				userDTO.getEmail(),
				userDTO.getPassword(),
				convertedRoles);
	}

}
