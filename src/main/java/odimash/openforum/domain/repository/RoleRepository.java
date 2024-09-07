package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Rights;
import odimash.openforum.domain.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByRightsIn(List<Rights> rights);

    // @Query("SELECT r.users FROM Role r WHERE r.id = :id")
    // Set<User> findUsersById(@Param("id") Long id);
}
