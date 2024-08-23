package odimash.openforum.domain.repository;

import odimash.openforum.domain.entity.Forum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ForumRepositoryTest {

    @Autowired
    private ForumRepository forumRepository;

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
        Forum forum = new Forum();
        forum.setName("Specific Name");
        forumRepository.save(forum);

        Optional<Forum> foundForum = forumRepository.findByName("Specific Name");
        assertThat(foundForum).isPresent();
        assertThat(foundForum.get().getName()).isEqualTo("Specific Name");
    }

    @Test
    void testFindSubCategoriesById() {
        Forum parentForum = new Forum();
        parentForum.setName("Parent Forum");
        forumRepository.save(parentForum);

        Forum childForum = new Forum();
        childForum.setName("Child Forum");
        childForum.setParentForum(parentForum);
        forumRepository.save(childForum);

        List<Forum> subCategories = forumRepository.findSubCategoriesById(parentForum.getId());

        assertThat(subCategories).hasSize(1);
        assertThat(subCategories.get(0).getName()).isEqualTo("Child Forum");
    }

    @Test
    void testDelete() {
        Forum forum = new Forum();
        forum.setName("To be deleted");
        forumRepository.save(forum);

        forumRepository.delete(forum);
        Optional<Forum> foundForum = forumRepository.findById(forum.getId());
        assertThat(foundForum).isNotPresent();
    }
}
