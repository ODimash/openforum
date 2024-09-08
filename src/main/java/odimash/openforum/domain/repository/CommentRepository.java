package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findByTopicId(Long id);
    Optional<Comment> findByAuthor(User author);
}
