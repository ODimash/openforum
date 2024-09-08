package odimash.openforum.service;

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
import odimash.openforum.domain.repository.CommentRepository;
import odimash.openforum.infrastructure.database.dto.CommentDTO;
import odimash.openforum.infrastructure.database.mapper.CommentMapper;
import odimash.openforum.exception.EntityNotFoundByIdException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

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
    public void testGetCommentById_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.mapToDTO(comment)).thenReturn(commentDTO);

        CommentDTO result = commentService.getCommentById(1L);
        assertThat(result).isEqualTo(commentDTO);
        verify(commentRepository, times(1)).findById(1L);
        verify(commentMapper, times(1)).mapToDTO(comment);
    }

    @Test
    public void testGetCommentById_NotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundByIdException.class, () -> commentService.getCommentById(1L));
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCommentsByTopicId_Success() {
        Set<Comment> comments = Collections.singleton(comment);
        when(commentRepository.findByTopicId(1L)).thenReturn(comments);
        when(commentMapper.mapToDTO(comment)).thenReturn(commentDTO);

        Set<CommentDTO> result = commentService.getCommentsByTopicId(1L);
        assertThat(result).contains(commentDTO);
        verify(commentRepository, times(1)).findByTopicId(1L);
        verify(commentMapper, times(1)).mapToDTO(comment);
    }

    @Test
    public void testCreateComment_Success() {
        when(commentMapper.mapToEntity(commentDTO)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.mapToDTO(comment)).thenReturn(commentDTO);

        CommentDTO result = commentService.createComment(commentDTO);
        assertThat(result).isEqualTo(commentDTO);
        verify(commentMapper, times(1)).mapToEntity(commentDTO);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).mapToDTO(comment);
    }

    @Test
    public void testDeleteComment_Success() {
        when(commentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).existsById(1L);
        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteComment_NotFound() {
        when(commentRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundByIdException.class, () -> commentService.deleteComment(1L));
        verify(commentRepository, times(1)).existsById(1L);
    }

    @Test
    public void testEditComment_Success() {
        String newContent = "Updated content";
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        comment.setContent(newContent);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.mapToDTO(comment)).thenReturn(new CommentDTO(1L, 1L, 1L, newContent));

        CommentDTO result = commentService.editComment(1L, newContent);
        assertThat(result.getContent()).isEqualTo(newContent);
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).mapToDTO(comment);
    }

    @Test
    public void testEditComment_NotFound() {
        String newContent = "Updated content";
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundByIdException.class, () -> commentService.editComment(1L, newContent));
        verify(commentRepository, times(1)).findById(1L);
    }
}
