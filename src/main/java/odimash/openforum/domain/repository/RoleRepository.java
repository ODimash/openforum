package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Rights;
import odimash.openforum.domain.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByRightsIn(List<Rights> rights);
}
