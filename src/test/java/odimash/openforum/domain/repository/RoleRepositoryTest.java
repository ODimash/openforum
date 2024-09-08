package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.entity.Rights;
import odimash.openforum.domain.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setName("Test Role");
    }

    @Test
    void testFindById() {
        roleRepository.save(role);
        Optional<Role> foundRole = roleRepository.findById(role.getId());
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(role.getName());
    }

    @Test
    void testFindByName() {
        roleRepository.save(role);
        Optional<Role> foundRole = roleRepository.findByName(role.getName());
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(role.getName());
    }

    @Test
    void testDelete() {
        roleRepository.save(role);
        Optional<Role> foundRole = roleRepository.findById(role.getId());
        assertThat(foundRole).isPresent();

        roleRepository.delete(role);
        foundRole = roleRepository.findById(role.getId());
        assertThat(foundRole).isNotPresent();
    }

    @Test
    void testFindByRightsIn() {
        Set<Rights> rights = Set.of(Rights.createCategory, Rights.writeCommentAnywhere);
        role.setRights(rights);
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByRightsIn(List.copyOf(rights));
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(role.getName());
    }

    @Test
    void testFindUsersById() {
        User user1 = new User(null, "user1", null, null, null);
        User user2 = new User(null, "user2", null, null, null);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Set<User> users = Set.of(user1, user2);
        role.setUsers(users);
        role = roleRepository.save(role);

        Optional<Role> optionalRole = roleRepository.findById(role.getId());
        assertThat(optionalRole).isPresent();

        Role foundRole = optionalRole.get();
        Set<User> foundUsers = foundRole.getUsers();

        assertThat(foundUsers).isEqualTo(users);
    }
}
