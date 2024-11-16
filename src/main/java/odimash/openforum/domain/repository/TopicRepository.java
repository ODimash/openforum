package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByForumId(Long forumId);
    Optional<Topic> findByTitle(String name);
}
