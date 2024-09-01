package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumRepository forumRepository;

    private Topic topic;
    private User author;
    private Comment comment;
    private Forum forum;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setUsername("Comment Author");
        userRepository.save(author);

        forum = new Forum();
        forum.setName("Test Forum");
        forumRepository.save(forum);

        topic = new Topic();
        topic.setName("Test Topic");
        topic.setComments(new HashSet<>());
        topic.setAuthor(author);
        topic.setForum(forum);
        topicRepository.save(topic);

        comment = new Comment();
        comment.setContent("Test Comment");
        comment.setAuthor(author);
        comment.setTopic(topic);
        commentRepository.save(comment);
    }

    @Test
    void testFindByAuthor() {
        Optional<Comment> foundComment = commentRepository.findByAuthor(author);
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getAuthor().getId()).isEqualTo(author.getId());
    }

    @Test
    void testFindByTopic() {
        Set<Comment> foundComments = commentRepository.findByTopic(topic);
        assertThat(foundComments).isNotEmpty();
        assertThat(foundComments.iterator().next().getTopic().getId()).isEqualTo(topic.getId());
    }

    @Test
    void testFindByTopicId_WhenNotComments() {
        commentRepository.delete(comment);
        Set<Comment> foundComments = commentRepository.findByTopicId(topic.getId());

        assertThat(foundComments).isNotEqualTo(null);
        assertThat(foundComments).isEqualTo(new HashSet<Comment>());
        assertThat(foundComments).isEmpty();
    }
}
