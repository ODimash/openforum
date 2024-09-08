package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    User user;

    @BeforeEach
    public void setUp() {
        user = new User(null, "Test user", "test@example.com", "password", null);
    }

    @Test
    void testFindById() {
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("Test user");
    }

    @Test
    void testFindByUsername() {
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testFindByEmail() {
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testFindRolesById() {
        Role role = new Role(null, "Test Role", null, null, null);
        roleRepository.save(role);

        user.setRoles(Set.of(role));
        userRepository.save(user);

        Set<Role> roles = userRepository.findRolesById(user.getId());
        Optional<Role> foundRole = roleRepository.findById(role.getId());

        assertThat(foundRole).isNotEmpty();
        assertThat(roles).contains(role);
    }

    @Test
    void testDelete() {
        userRepository.save(user);
        userRepository.delete(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isNotPresent();
    }

}
