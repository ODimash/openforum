package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("SELECT f FROM Forum f WHERE f.parentForum.id = :parentId")
    List<Forum> findSubCategoriesById(@Param("parentId") Long parentId);

    Optional<Forum> findByNameAndParentForumId(String name, Long parentForumId);
    Optional<Forum> findByName(String name);
}
