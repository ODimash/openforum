package odimash.openforum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.TopicRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.TopicDTO;
import odimash.openforum.infrastructure.database.mapper.TopicMapper;

@SpringBootTest
public class TopicServiceTest {

	@Mock
	TopicRepository topicRepository;

	@Mock
	TopicMapper topicMapper;

	@InjectMocks
	TopicService topicService;

	Topic topic;
	TopicDTO topicDTO;
	Forum forum;
	User user;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		user = new User(1L, "Test user", "Test email", "Test password", null);
		forum = new Forum(1L, "Test forum", null, null, null);

		topic = new Topic(1L, "Test topic", forum, "test content", user, new HashSet<Comment>());
		topicDTO = new TopicDTO(1L, "Test topic", 1L,"test content", 1L);

		forum.setTopics(List.of(topic));

	}


	@Test
	public void testCreateTopic_Success() {
		when(topicMapper.mapToEntity(topicDTO)).thenReturn(topic);
		when(topicMapper.mapToDTO(topic)).thenReturn(topicDTO);
		when(topicRepository.save(topic)).thenReturn(topic);
		assertThat(topicService.createTopic(topicDTO)).isEqualTo(topicDTO);
	}

	@Test
	public void testUpdateTopic_Success() {
		when(topicRepository.findById(topicDTO.getId())).thenReturn(Optional.of(topic));
		when(topicMapper.mapToEntity(topicDTO)).thenReturn(topic);
		when(topicRepository.save(topic)).thenReturn(topic);
		when(topicMapper.mapToDTO(topic)).thenReturn(topicDTO);

		TopicDTO updatedTopicDTO = topicService.updateTopic(topicDTO);
		assertThat(updatedTopicDTO).isEqualTo(topicDTO);
	}

	@Test
	public void testUpdateTopic_ShouldReturnThrowException_WhenIdIsNull() {

		topicDTO.setId(null);

		Exception thrown = assertThrows(
			IllegalArgumentException.class,
			() -> topicService.updateTopic(topicDTO)
		);

		assertThat(thrown.getMessage()).isEqualTo("ID from TopicDTO is null");

	}

	@Test
	public void testUpdateTopic_ShouldReturnThrowException_WhenEntityNotFoundById() {

		when(topicMapper.mapToEntity(topicDTO)).thenReturn(topic);
		when(topicRepository.findById(topicDTO.getId())).thenReturn(Optional.empty());

		Exception thrown = assertThrows(
			EntityNotFoundByIdException.class,
			() -> topicService.updateTopic(topicDTO)
		);

		assertThat(thrown.getMessage()).isEqualTo(
			String.format("Not found \"%s\" by ID %s", Topic.class.getSimpleName(), topicDTO.getId().toString())
		);
	}

	@Test
	public void testReadTopic_Success() {
		when(topicRepository.findById(topicDTO.getId())).thenReturn(Optional.of(topic));
		when(topicMapper.mapToDTO(topic)).thenReturn(topicDTO);
		TopicDTO foundTopicDTO = topicService.readTopic(topicDTO.getId());
		assertThat(foundTopicDTO).isEqualTo(topicDTO);
	}

	@Test
	public void testReadTopic_ShouldReturnThrow_WhenIdIsNull() {

		Exception thrown = assertThrows(
			IllegalArgumentException.class,
			() -> topicService.readTopic(null)
		);

		assertThat(thrown.getMessage()).isEqualTo("ID for search topic is null");
	}

	@Test
	public void testReadTopic_ShouldReturnThrow_WhenEntityNotFoundById() {

		when(topicRepository.findById(topic.getId())).thenReturn(Optional.empty());

		Exception thrown = assertThrows(
			EntityNotFoundByIdException.class,
			() -> topicService.readTopic(topic.getId())
		);

		assertThat(thrown.getMessage()).isEqualTo(
			String.format("Not found \"%s\" by ID %s", Topic.class.getSimpleName(), topic.getId().toString())
		);
	}

	@Test
	public void testDeleteTopic_Success() {
		doNothing().when(topicRepository).deleteById(topic.getId());
		topicService.deleteTopic(topic.getId());
		verify(topicRepository, times(1)).deleteById(topic.getId());
	}

	@Test
	public void testDeleteTopic_WhenIdIsNull() {

		Exception thrown = assertThrows(
			IllegalArgumentException.class,
			() -> topicService.deleteTopic(null)
		);
		assertThat(thrown.getMessage()).isEqualTo("ID for delete topic can not be null");
	}

}
