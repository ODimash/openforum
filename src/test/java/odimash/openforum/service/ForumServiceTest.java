package odimash.openforum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.ForumDTO;
import odimash.openforum.infrastructure.database.mapper.ForumMapper;

@SpringBootTest
@Transactional
public class ForumServiceTest {

    @Mock
    private ForumRepository forumRepository;

    @Mock
    private ForumMapper forumMapper;

    @InjectMocks
    private ForumService forumService;

    private ForumDTO forumDTO;
    private Forum forum;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        forumDTO = new ForumDTO(1L, "Test forum", null);
        forum = new Forum(1L, "Test forum", null, null, null);
    }

    @Test
    public void testCreateForum_ShouldReturnForumDTO_WhenForumDontExist() {
        when(forumRepository.findByNameAndParentForumId(forumDTO.getName(), forumDTO.getParentForumId())).thenReturn(Optional.empty());
        when(forumMapper.mapToEntity(forumDTO)).thenReturn(forum);
        when(forumRepository.save(forum)).thenReturn(forum);
        when(forumMapper.mapToDTO(forum)).thenReturn(forumDTO);

        ForumDTO result = forumService.createForum(forumDTO);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(forumDTO);
        verify(forumRepository, times(1)).findByNameAndParentForumId(forumDTO.getName(), forumDTO.getParentForumId());
        verify(forumRepository, times(1)).save(forum);

    }

    @Test
    public void testCreateForum_ShouldThrowException_WhenForumAlreadyExist() {
        when(forumRepository.findByNameAndParentForumId(forumDTO.getName(), forumDTO.getParentForumId())).thenReturn(Optional.of(forum));

        EntityNameIsAlreadyTakenException thrown = assertThrows(
            EntityNameIsAlreadyTakenException.class,
            () -> forumService.createForum(forumDTO)
        );

        assertThat(thrown.getMessage()).isEqualTo("The name \"Test forum\" of the \"Forum\" is already taken");
    }

    @Test
    public void testUpdateForum_ShouldReturnForumDTO_WhenForumIsExist() {
        when(forumRepository.findById(forumDTO.getId())).thenReturn(Optional.of(forum));
        when(forumRepository.save(forum)).thenReturn(forum);
        when(forumMapper.mapToEntity(forumDTO)).thenReturn(forum);
        when(forumMapper.mapToDTO(forum)).thenReturn(forumDTO);

        ForumDTO result = forumService.updateForum(forumDTO);

        assertThat(result).isEqualTo(forumDTO);
    }

    @Test
    public void testUpdateForum_ShouldThrowException_WhenIdIsNull() {
        forumDTO.setId(null);

        Exception thrown = assertThrows(
            IllegalArgumentException.class,
            () -> forumService.updateForum(forumDTO)
        );

        assertThat(thrown.getMessage()).isEqualTo("Forum ID can not be null for update");
    }

    @Test
    public void testReadForum_ShouldReturnForumDTO_WhenForumIsExist() {
        when(forumRepository.findById(forumDTO.getId())).thenReturn(Optional.of(forum));
        when(forumMapper.mapToDTO(forum)).thenReturn(forumDTO);

        ForumDTO result = forumService.readForumById(forumDTO.getId());

        assertThat(result).isEqualTo(forumDTO);

        verify(forumRepository, times(1)).findById(forumDTO.getId());
        verify(forumMapper, times(1)).mapToDTO(forum);
    }

    @Test
    public void testReadForum_ShouldThrowException_WhenEntityDoesNotExist() {
        when(forumRepository.findById(forumDTO.getId())).thenReturn(Optional.empty());

        Exception thrown = assertThrows(
            EntityNotFoundByIdException.class,
            () -> forumService.readForumById(forumDTO.getId())
        );

        assertThat(thrown.getMessage()).isEqualTo(
            String.format("Not found \"%s\" by ID %s", Forum.class.getSimpleName(), forumDTO.getId().toString())
        );

        verify(forumRepository, times(1)).findById(forumDTO.getId());
        verify(forumMapper, never()).mapToDTO(forum);
    }

    @Test
    public void testDeleteForum_ShouldBeSuccess() {
        doNothing().when(forumRepository).deleteById(forumDTO.getId());
        forumService.deleteForum(forumDTO.getId());
        verify(forumRepository, times(1)).deleteById(forumDTO.getId());
    }

    @Test
    void testGetPathAStringSuccess() {
        Long forumId = 3L;

        Forum forum1 = new Forum(1L, "Forum 1", null, null, null);
        Forum forum2 = new Forum(2L, "Forum 2", forum1, null, null);
        Forum forum3 = new Forum(3L, "Forum 3", forum2, null, null);
        forum1.setSubCategories(List.of(forum2));
        forum2.setSubCategories(List.of(forum3));

        ForumDTO forumDTO1 = new ForumDTO(1L, "Forum 1", null);
        ForumDTO forumDTO2 = new ForumDTO(2L, "Forum 2", 1L);
        ForumDTO forumDTO3 = new ForumDTO(3L, "Forum 3", 2L);

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum3));
        when(forumMapper.mapToDTO(forum1)).thenReturn(forumDTO1);
        when(forumMapper.mapToDTO(forum2)).thenReturn(forumDTO2);
        when(forumMapper.mapToDTO(forum3)).thenReturn(forumDTO3);

        List<String> result = forumService.getPathAString(forumId);
        assertThat(result).containsExactly("Forum 1", "Forum 2", "Forum 3");

        verify(forumRepository, times(1)).findById(forumId);
        verify(forumMapper, times(1)).mapToDTO(forum1);
        verify(forumMapper, times(1)).mapToDTO(forum2);
        verify(forumMapper, times(1)).mapToDTO(forum3);
    }

    @Test
    void testGetPathAStringThrowsException() {
        Long forumId = 1L;

        when(forumRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> forumService.getPathAString(forumId))
                .isInstanceOf(EntityNotFoundByIdException.class)
                .hasMessageContaining(String.format("Not found \"%s\" by ID %s", Forum.class.getSimpleName(), forumId.toString()));

        verify(forumRepository, times(1)).findById(forumId);
        verify(forumMapper, never()).mapToDTO(any(Forum.class));
    }

    @Test
    void testGetPathAsDTOSuccess() {
        Long forumId = 3L;

        Forum forum1 = new Forum(1L, "Forum 1", null, null, null);
        Forum forum2 = new Forum(2L, "Forum 2", forum1, null, null);
        Forum forum3 = new Forum(3L, "Forum 3", forum2, null, null);
        forum1.setSubCategories(List.of(forum2));
        forum2.setSubCategories(List.of(forum3));

        ForumDTO forumDTO1 = new ForumDTO(1L, "Forum 1", null);
        ForumDTO forumDTO2 = new ForumDTO(2L, "Forum 2", 1L);
        ForumDTO forumDTO3 = new ForumDTO(3L, "Forum 3", 2L);

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum3));
        when(forumMapper.mapToDTO(forum1)).thenReturn(forumDTO1);
        when(forumMapper.mapToDTO(forum2)).thenReturn(forumDTO2);
        when(forumMapper.mapToDTO(forum3)).thenReturn(forumDTO3);

        List<ForumDTO> result = forumService.getPathAsDTO(forumId);
        assertThat(result).containsExactly(forumDTO1, forumDTO2, forumDTO3);

        verify(forumRepository, times(1)).findById(forumId);
        verify(forumMapper, times(1)).mapToDTO(forum1);
        verify(forumMapper, times(1)).mapToDTO(forum2);
        verify(forumMapper, times(1)).mapToDTO(forum3);
    }

    @Test
    void testGetPathAsDTOThrowsException() {
        Long forumId = 2L;

        when(forumRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> forumService.getPathAsDTO(forumId))
                .isInstanceOf(EntityNotFoundByIdException.class)
                .hasMessageContaining(String.format("Not found \"%s\" by ID %s", Forum.class.getSimpleName(), forumId.toString()));

        verify(forumRepository, times(1)).findById(forumId);
        verify(forumMapper, never()).mapToDTO(any(Forum.class));
    }

}
