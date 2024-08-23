package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("SELECT f FROM Forum f WHERE f.parentForum.id = :parentId")
    List<Forum> findSubCategoriesById(@Param("parentId") Long parentId);

    Optional<Forum> findByName(String name);
}
