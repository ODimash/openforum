package odimash.openforum.infrastructure.database.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.domain.repository.RoleRepository;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.RoleDTO;

@Component
public class RoleMapper {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ForumRepository forumRepository;

	@Autowired
	UserRepository userRepository;

	public RoleDTO mapToDTO(Role role) {
		return role == null ? null : new RoleDTO(
			role.getId(),
			role.getName(),
			role.getControlledForum() == null ? null : role.getControlledForum().getId(),
			role.getRights()
		);
	}

	public Role mapToEntity(RoleDTO roleDTO) {
		return roleDTO == null ? null : new Role(
			roleDTO.getId(),
			roleDTO.getName(),
			roleDTO.getControlledForumId() == null ?
				null : forumRepository.findById(roleDTO.getControlledForumId())
				.orElseThrow(() -> new EntityNotFoundByIdException(Forum.class, roleDTO.getControlledForumId())),

			roleDTO.getId() == null ? null : roleRepository.findById(roleDTO.getId())
				.orElseThrow(() -> new EntityNotFoundByIdException(Role.class, roleDTO.getId()))
				.getUsers(),

			roleDTO.getRights()
		);
	}

}
