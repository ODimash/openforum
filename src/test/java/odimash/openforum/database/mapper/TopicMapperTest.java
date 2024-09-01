package odimash.openforum.database.mapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.repository.CommentRepository;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.infrastructure.database.dto.TopicDTO;
import odimash.openforum.infrastructure.database.mapper.TopicMapper;

@SpringBootTest
public class TopicMapperTest {

	@Mock
	ForumRepository forumRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	CommentRepository commentRepository;

	@InjectMocks
	TopicMapper topicMapper;

	Topic topic;
	TopicDTO topicDTO;
	Set<Comment> comments;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		comments = new HashSet<Comment>();
		topic = new Topic(1L, "Test topic", null, "test content", null, comments);
		topicDTO = new TopicDTO(1L, "Test topic", null, "test content", null);
	}

	@Test
	public void testMapToTopicDTO() {
		TopicDTO convertedTopicDTO = topicMapper.mapToDTO(topic);
		assertThat(convertedTopicDTO).isEqualTo(topicDTO);
	}

	@Test
	public void testMapToTopicEntity_Success() {

		when(commentRepository.findByTopicId(1L)).thenReturn(comments);

		Topic convertedTopic = topicMapper.mapToEntity(topicDTO);
		assertThat(convertedTopic).isEqualTo(topic);
	}

}
