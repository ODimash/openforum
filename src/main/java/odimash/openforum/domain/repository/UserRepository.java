package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Role;
import odimash.openforum.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u.roles FROM User u WHERE u.id = :id")
    Set<Role> findRolesById(@Param("id") Long id);
}
