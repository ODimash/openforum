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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDTO createUser(UserDTO userDTO) {
        verifyDataCorrectness(userDTO);

        logger.info("Creating user with username: {}", userDTO.getUsername());
        User savedUser = userRepository.save(userMapper.mapToEntity(userDTO));
        logger.info("User created with ID: {}", savedUser.getId());
        return userMapper.mapToDTO(savedUser);
    }

    public UserDTO readUser(Long id) {
        if (id == null) {
            logger.error("User ID cannot be null for read");
            throw new IllegalArgumentException("User ID cannot be null for read");
        }

        logger.info("Reading user with ID: {}", id);
        User foundUser = userRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("User with ID '{}' not found", id);
                return new EntityNotFoundByIdException(User.class, id);
            });

        return userMapper.mapToDTO(foundUser);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            logger.error("User ID cannot be null for update");
            throw new IllegalArgumentException("User ID cannot be null for update");
        }

        logger.info("Updating user with ID: {}", userDTO.getId());
        userRepository.findById(userDTO.getId())
            .orElseThrow(() -> {
                logger.error("User with ID '{}' not found", userDTO.getId());
                return new EntityNotFoundByIdException(User.class, userDTO.getId());
            });

        User updatedUser = userRepository.save(userMapper.mapToEntity(userDTO));
        logger.info("User updated with ID: {}", updatedUser.getId());
        return userMapper.mapToDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (id == null) {
            logger.error("User ID cannot be null for delete");
            throw new IllegalArgumentException("User ID cannot be null for delete");
        }

        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }

    private void verifyDataCorrectness(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            logger.error("Username '{}' is already taken", userDTO.getUsername());
            throw new EntityNameIsAlreadyTakenException(User.class, userDTO.getUsername());
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            logger.error("Email '{}' is already used", userDTO.getEmail());
            throw new EmailAlreadyUsedException(userDTO.getEmail());
        }
        if (userDTO.getPassword().length() < 6) {
            logger.error("Password format is incorrect, it should be at least 6 characters long");
            throw new WrongPasswordFormatException();
        }
    }
}
