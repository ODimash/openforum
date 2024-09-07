package odimash.openforum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.exception.EmailAlreadyUsedException;
import odimash.openforum.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.exception.WrongPasswordFormatException;
import odimash.openforum.infrastructure.database.dto.UserDTO;
import odimash.openforum.infrastructure.database.mapper.UserMapper;

public class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@Mock
	UserMapper userMapper;

	@InjectMocks
	UserService userService;

	User user;
	UserDTO userDTO;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		user = new User(1L, "test user", "email", "password", null);
		userDTO = new UserDTO(1L, "test user", "email", "password");

		when(userMapper.mapToDTO(user)).thenReturn(userDTO);
		when(userMapper.mapToEntity(userDTO)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);
	}

	@Test
	public void testCreateUser_Success() {
		UserDTO createdUser = userService.createUser(userDTO);
		assertThat(createdUser).isEqualTo(userDTO);
	}

	@Test
	public void testCreateUser_WhenUsernameAlreadyExist() {
		when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));

		Exception thrown = assertThrows(
			EntityNameIsAlreadyTakenException.class,
			() -> userService.createUser(userDTO));

		assertThat(thrown.getMessage()).isEqualTo(
			String.format("The name \"test user\" of the \"User\" is already taken"));
	}

	@Test
	public void testCreateUser_WhenEmailAlreadyUsed() {
		when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

		Exception thrown = assertThrows(
			EmailAlreadyUsedException.class,
			() -> userService.createUser(userDTO));

		assertThat(thrown.getMessage()).isEqualTo(
			String.format("Email \"%s\" is already used", userDTO.getEmail()));

	}

	@Test
	public void testCreateUser_WhenPasswordTooShort() {
		userDTO.setPassword("test");

		Exception thrown = assertThrows(
			WrongPasswordFormatException.class,
			() -> userService.createUser(userDTO));

		assertThat(thrown.getMessage()).isEqualTo(
			String.format("Password is too short"));
	}

	@Test
	public void testReadUser_Success() {
		when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
		UserDTO foundUser = userService.readUser(userDTO.getId());
		assertThat(foundUser).isEqualTo(userDTO);
	}

	@Test
	public void testReadUser_WhenIdNull() {
		Exception thrown = assertThrows(IllegalArgumentException.class, () -> userService.readUser(null));
		assertThat(thrown.getMessage()).isEqualTo("User ID can not be null for read");
	}

	@Test
	public void testReadUser_WhemUserNotFound() {
		when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());
		Exception thrown = assertThrows(EntityNotFoundByIdException.class, () -> userService.readUser(userDTO.getId()));
		assertThat(thrown.getMessage()).isEqualTo(String.format("Not found \"%s\" by ID %s", User.class.getSimpleName(), userDTO.getId()));
	}

	@Test
	public void testUpdateUser_Success() {
		when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
		UserDTO updatedUserDTO = userService.updateUser(userDTO);
		assertThat(updatedUserDTO).isEqualTo(userDTO);
	}

	@Test
	public void testUpdateUser_WhenIdNull() {
		userDTO.setId(null);
		Exception thrown = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userDTO));
		assertThat(thrown.getMessage()).isEqualTo("User ID can not be null for update");
	}

	@Test
	public void testUpdateUser_WhenUserNotFound() {
		when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());
		Exception thrown = assertThrows(EntityNotFoundByIdException.class, () -> userService.updateUser(userDTO));
		assertThat(thrown.getMessage()).isEqualTo(String.format("Not found \"%s\" by ID %s", User.class.getSimpleName(), userDTO.getId()));
	}

	@Test
	public void testDeleteUser_Success() {
		doNothing().when(userRepository).deleteById(userDTO.getId());
		userService.deleteUser(userDTO.getId());
		verify(userRepository, times(1)).deleteById(userDTO.getId());
	}

	@Test
	public void testDeleteUser_WhereIdNull() {
		userDTO.setId(null);
		doThrow(IllegalArgumentException.class).when(userRepository).deleteById(userDTO.getId());

		Exception thrown = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userDTO.getId()));
		assertThat(thrown.getMessage()).isEqualTo("User ID can not be null for delete");
		verify(userRepository, never()).deleteById(userDTO.getId());
	}

}
