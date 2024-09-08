package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Set<Topic> findByForum(Forum forum);
    Optional<Topic> findByTitle(String name);
}
