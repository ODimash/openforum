package odimash.openforum.infrastructure.database.mapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.infrastructure.database.dto.UserDTO;

public class UserMapperTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserMapper userMapper;

	User user;
	UserDTO userDTO;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		user = new User(1L, "test user", null, null, null);
		userDTO = new UserDTO(1L, "test user", null, null, null);
	}

	@Test
	public void testMapToDTO() {
		UserDTO convertedUserDTO = userMapper.mapToDTO(user);
		assertThat(convertedUserDTO).isEqualTo(userDTO);
	}

	@Test
	public void testMapToEntity() {
		when(userRepository.findRolesById(1L)).thenReturn(null);

		User convertedUser = userMapper.mapToEntity(userDTO);
		assertThat(convertedUser).isEqualTo(user);
	}
}
