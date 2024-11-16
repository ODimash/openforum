package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private UserRepository userRepository;

    private Forum forum;
    private User author;
    private Topic topic;

    @BeforeEach
    void setUp() {
        tearDown();
        forum = new Forum();
        forum.setName("Test Forum");
        forumRepository.save(forum);

        author = new User();
        author.setUsername("Test Author");
        userRepository.save(author);

        topic = new Topic();
        topic.setTitle("Test Topic");
        topic.setForum(forum);
        topic.setAuthor(author);
        topic.setComments(new HashSet<>());
        topicRepository.save(topic);
    }

    @AfterEach
    void tearDown() {
        forumRepository.deleteAll();
    }

    @Test
    void testFindByName() {
        Optional<Topic> foundTopic = topicRepository.findByTitle("Test Topic");
        assertThat(foundTopic).isPresent();
        assertThat(foundTopic.get().getTitle()).isEqualTo("Test Topic");
    }

    @Test
    void testFindByForumId() {
        List<Topic> foundTopics = topicRepository.findByForumId(forum.getId());
        assertThat(foundTopics).isNotEmpty();
        assertThat(foundTopics.iterator().next().getForum().getId()).isEqualTo(forum.getId());
    }
}
