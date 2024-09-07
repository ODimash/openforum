package odimash.openforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.exception.EmailAlreadyUsedException;
import odimash.openforum.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.exception.WrongPasswordFormatException;
import odimash.openforum.infrastructure.database.dto.UserDTO;
import odimash.openforum.infrastructure.database.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserMapper userMapper;

	public UserDTO createUser(UserDTO userDTO) {
		try {
			checkDataCorrectness(userDTO);
		} catch (Exception e) {
			throw e;
		}

		User savedUser = userRepository.save(userMapper.mapToEntity(userDTO));
		return userMapper.mapToDTO(savedUser);

	}

	public UserDTO readUser(Long id) {
		if (id == null)
			throw new IllegalArgumentException("User ID can not be null for read");

		User foundUser = userRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundByIdException(User.class, id));

		return userMapper.mapToDTO(foundUser);
	}

	public UserDTO updateUser(UserDTO userDTO) {
		if (userDTO.getId() == null)
			throw new IllegalArgumentException("User ID can not be null for update");

		userRepository.findById(userDTO.getId()).orElseThrow(() -> new EntityNotFoundByIdException(User.class, userDTO.getId()));

		User updatedUser = userRepository.save(userMapper.mapToEntity(userDTO));
		return userMapper.mapToDTO(updatedUser);

	}

	public void deleteUser(Long id) {
		if (id == null)
			throw new IllegalArgumentException("User ID can not be null for delete");

		userRepository.deleteById(id);
	}

	private void checkDataCorrectness(UserDTO userDTO) {
		if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
			throw new EntityNameIsAlreadyTakenException(User.class, userDTO.getUsername());
		if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
			throw new EmailAlreadyUsedException(userDTO.getEmail());
		if (userDTO.getPassword().length() < 6)
			throw new WrongPasswordFormatException();
	}

}
