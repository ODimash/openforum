package odimash.openforum.infrastructure.database.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.TopicRepository;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.CommentDTO;

import java.util.Optional;

public class CommentMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private CommentMapper commentMapper;

    private Comment comment;
    private CommentDTO commentDTO;
    private User author;
    private Topic topic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new User();
        author.setId(1L);

        topic = new Topic();
        topic.setId(1L);

        comment = new Comment(1L, author, topic, "Test content");
        commentDTO = new CommentDTO(1L, 1L, 1L, "Test content");
    }

    @Test
    public void testMapToDTO() {
        CommentDTO dto = commentMapper.mapToDTO(comment);
		assertThat(dto).isEqualTo(commentDTO);
    }

    @Test
    public void testMapToEntity_Success() {
        when(userRepository.findById(commentDTO.getAuthorId())).thenReturn(Optional.of(author));
        when(topicRepository.findById(commentDTO.getTopicId())).thenReturn(Optional.of(topic));

        Comment entity = commentMapper.mapToEntity(commentDTO);
		assertThat(entity).isEqualTo(comment);
    }

    @Test
    public void testMapToEntity_WhenAuthorNotFound() {
        when(userRepository.findById(commentDTO.getAuthorId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundByIdException.class, () -> commentMapper.mapToEntity(commentDTO));
    }

    @Test
    public void testMapToEntity_WhenTopicNotFound() {
        when(topicRepository.findById(commentDTO.getTopicId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundByIdException.class, () -> commentMapper.mapToEntity(commentDTO));
    }
}
