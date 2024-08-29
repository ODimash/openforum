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
    void testFindByNameAndParentForum() {
        Forum parentForum = new Forum();
        parentForum.setName("Parent Forum");
        forumRepository.save(parentForum);

        Forum expectedChildForum = new Forum();
        expectedChildForum.setName("Child Forum");
        expectedChildForum.setParentForum(parentForum);
        forumRepository.save(expectedChildForum);

        Forum otherChildForum = new Forum();
        otherChildForum.setName("Child Forum");
        forumRepository.save(otherChildForum);

        Optional<Forum> foundForum = forumRepository.findByNameAndParentForum(expectedChildForum.getName(), parentForum);

        assertThat(foundForum.get()).isEqualTo(expectedChildForum);
    }

    @Test
    void testFindByNameAndParentId() {
        Forum parentForum = new Forum();
        parentForum.setName("Parent Forum");
        forumRepository.save(parentForum);

        Forum expectedChildForum = new Forum();
        expectedChildForum.setName("Child Forum");
        expectedChildForum.setParentForum(parentForum);
        forumRepository.save(expectedChildForum);

        Forum otherChildForum = new Forum();
        otherChildForum.setName("Child Forum");
        forumRepository.save(otherChildForum);

        Optional<Forum> foundForum = forumRepository.findByNameAndParentForumId(expectedChildForum.getName(), parentForum.getId());

        assertThat(foundForum.get()).isEqualTo(expectedChildForum);
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
