package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("uniqueUsername");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("uniqueUsername");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("uniqueUsername");
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setUsername("testName");
        user.setEmail("testEmail");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("testEmail");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("testEmail");
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setUsername("tobedeleted");
        userRepository.save(user);

        userRepository.delete(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isNotPresent();
    }
}
