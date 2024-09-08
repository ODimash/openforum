package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Forum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ForumRepositoryTest {

    @Autowired
    private ForumRepository forumRepository;

    private Forum parentForum;
    private Forum childForum;

    @BeforeEach
    void setUp() {
        parentForum = new Forum();
        parentForum.setName("Parent Forum");
        forumRepository.save(parentForum);

        childForum = new Forum();
        childForum.setName("Child Forum");
        childForum.setParentForum(parentForum);
        forumRepository.save(childForum);
    }

    @Test
    void testSaveAndFindById() {
        Forum forum = new Forum();
        forum.setName("Test Forum");
        forumRepository.save(forum);

        Optional<Forum> foundForum = forumRepository.findById(forum.getId());
        assertThat(foundForum).isPresent();
        assertThat(foundForum.get().getName()).isEqualTo("Test Forum");
    }

    @Test
    void testFindByName() {
        Optional<Forum> foundForum = forumRepository.findByName("Parent Forum");
        assertThat(foundForum).isPresent();
        assertThat(foundForum.get().getName()).isEqualTo("Parent Forum");
    }

    @Test
    void testFindSubCategoriesById() {
        List<Forum> subCategories = forumRepository.findSubCategoriesById(parentForum.getId());

        assertThat(subCategories).hasSize(1);
        assertThat(subCategories.get(0).getName()).isEqualTo("Child Forum");
    }

    @Test
    void testFindByNameAndParentForum() {
        Optional<Forum> foundForum = forumRepository.findByNameAndParentForumId(childForum.getName(), parentForum.getId());

        assertThat(foundForum).isPresent();
        assertThat(foundForum.get()).isEqualTo(childForum);
    }

    @Test
    void testFindByNameAndParentId() {
        Optional<Forum> foundForum = forumRepository.findByNameAndParentForumId(childForum.getName(), parentForum.getId());

        assertThat(foundForum).isPresent();
        assertThat(foundForum.get()).isEqualTo(childForum);
    }

    @Test
    void testDelete() {
        forumRepository.delete(childForum);
        Optional<Forum> foundForum = forumRepository.findById(childForum.getId());
        assertThat(foundForum).isNotPresent();
    }

    @Test
    void testFindByNonExistentId() {
        Optional<Forum> foundForum = forumRepository.findById(-1L);
        assertThat(foundForum).isNotPresent();
    }

}
