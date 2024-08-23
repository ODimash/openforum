package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Rights;
import odimash.openforum.domain.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveAndFindById() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findById(role.getId());
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testFindByName() {
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByName("ROLE_USER");
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo("ROLE_USER");
    }

    @Test
    void testDelete() {
        Role role = new Role();
        role.setName("ROLE_MODERATOR");
        roleRepository.save(role);

        roleRepository.delete(role);
        Optional<Role> foundRole = roleRepository.findById(role.getId());
        assertThat(foundRole).isNotPresent();
    }

    @Test
    void testFindByRightsIn() {
        Role role = new Role();
        role.setName("ROLE_WITH_RIGHTS");
        Set<Rights> rights = new HashSet<Rights>();
        rights.add(Rights.createCategory);
        rights.add(Rights.writeCommentAnywhere);
        role.setRights(rights);
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByRightsIn(new ArrayList<Rights>(rights));
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(role.getName());
    }
}
