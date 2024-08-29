package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findByTopic(Topic topic);
    Optional<Comment> findByAuthor(User author);
}