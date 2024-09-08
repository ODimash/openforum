package odimash.openforum.infrastructure.database.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.domain.repository.RoleRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.RoleDTO;

public class RoleMapperTest {

	@Mock
	private ForumRepository forumRepository;

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private RoleMapper roleMapper;

	private Role role;
	private RoleDTO roleDTO;
	private Forum forum;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		forum = new Forum();
		forum.setId(1L);

		role = new Role(1L, "Admin", forum, Collections.emptySet(), Collections.emptySet());
		roleDTO = new RoleDTO(1L, "Admin", 1L, Collections.emptySet());
	}

	@Test
	public void testMapToDTO() {
		RoleDTO dto = roleMapper.mapToDTO(role);
		assertThat(dto).isEqualTo(roleDTO);
	}

	@Test
	public void testMapToEntity_Success() {
		when(forumRepository.findById(roleDTO.getControlledForumId())).thenReturn(Optional.of(forum));
		when(roleRepository.findById(roleDTO.getId())).thenReturn(Optional.of(role));
		Role entity = roleMapper.mapToEntity(roleDTO);
		assertThat(entity).isEqualTo(role);
	}

	@Test
	public void testMapToEntity_WhenForumNotFound() {
		when(forumRepository.findById(roleDTO.getControlledForumId())).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundByIdException.class, () -> roleMapper.mapToEntity(roleDTO));
	}

	@Test
	public void testMapToEntity_WhenRoleNotFound() {
		when(roleRepository.findById(roleDTO.getId())).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundByIdException.class, () -> roleMapper.mapToEntity(roleDTO));
	}

}
