package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
        topic.setName("Test Topic");
        topic.setForum(forum);
        topic.setAuthor(author);
        topic.setComments(new HashSet<>());
        topicRepository.save(topic);
    }

    @AfterEach
    void tearDown() {
        topicRepository.deleteAll();
        userRepository.deleteAll();
        forumRepository.deleteAll();
    }

    @Test
    void testFindByName() {
        Optional<Topic> foundTopic = topicRepository.findByName("Test Topic");
        assertThat(foundTopic).isPresent();
        assertThat(foundTopic.get().getName()).isEqualTo("Test Topic");
    }

    @Test
    void testFindByForum() {
        Set<Topic> foundTopics = topicRepository.findByForum(forum);
        assertThat(foundTopics).isNotEmpty();
        assertThat(foundTopics.iterator().next().getForum().getId()).isEqualTo(forum.getId());
    }
}