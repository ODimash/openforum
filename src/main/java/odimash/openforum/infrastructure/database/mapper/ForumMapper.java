package odimash.openforum.infrastructure.database.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.ForumDTO;

@Component
public class ForumMapper {

    @Autowired
    private ForumRepository forumRepository;

    public ForumDTO mapToDTO(Forum forum) {
        ForumDTO forumDTO = new ForumDTO();
        forumDTO.setId(forum.getId());
        forumDTO.setName(forum.getName());
        forumDTO.setParentForumId(forum.getParentForum() != null ? forum.getParentForum().getId() : null);
        return forumDTO;
    }

    public Forum mapToEntity(ForumDTO forumDTO) {
        Forum forum = new Forum();

        forum.setId(forumDTO.getId());
        forum.setName(forumDTO.getName());

        forum.setParentForum(forumDTO.getParentForumId() == null ?
            null : forumRepository.findById(forumDTO.getParentForumId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(Forum.class, forumDTO.getParentForumId())));

        forum.setSubCategories(forumRepository.findSubCategoriesById(forumDTO.getId()));

        return forum;
    }
}
