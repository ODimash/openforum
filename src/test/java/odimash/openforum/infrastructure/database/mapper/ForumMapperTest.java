package odimash.openforum.infrastructure.database.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.ForumDTO;


public class ForumMapperTest {

    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private ForumMapper forumMapper;

    private Forum forum;
    private Forum parentForum;
    private ForumDTO forumDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        parentForum = new Forum(1L, "Test parent forum", null, null, null);
        forum = new Forum(2L, "Test forum", parentForum, null, null);
        forumDTO = new ForumDTO(2L, "Test forum", parentForum.getId());

        when(forumRepository.findById(1L)).thenReturn(Optional.of(parentForum));
        when(forumRepository.findById(2L)).thenReturn(Optional.of(forum));
        when(forumRepository.findSubCategoriesById(2L)).thenReturn(null);
    }

    @Test
    public void testMapToDTO() {
        assertThat(forumMapper.mapToDTO(forum)).isEqualTo(forumDTO);
    }

    @Test
    public void testMapToEntity_Success() {
        Forum result = forumMapper.mapToEntity(forumDTO);
        assertThat(result).isEqualTo(forum);
    }

    @Test
    public void testMapToEntity_Thrown() {
        when(forumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> forumMapper.mapToEntity(forumDTO))
            .isInstanceOf(EntityNotFoundByIdException.class)
            .hasMessageContaining(String.format("Not found \"%s\" by ID %s", Forum.class.getSimpleName(), parentForum.getId().toString()));

        verify(forumRepository, never()).findSubCategoriesById(forum.getId());
    }

}
