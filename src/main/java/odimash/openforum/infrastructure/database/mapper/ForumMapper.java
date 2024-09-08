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
        return forum == null ? null : new ForumDTO(
            forum.getId(),
            forum.getName(),
            forum.getParentForum() == null ? null : forum.getParentForum().getId()
        );
    }

    public Forum mapToEntity(ForumDTO forumDTO) {
        return forumDTO == null ? null : new Forum(
            forumDTO.getId(),
            forumDTO.getName(),
            forumDTO.getParentForumId() == null ? null : forumRepository.findById(forumDTO.getParentForumId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(Forum.class, forumDTO.getParentForumId())),
            forumRepository.findSubCategoriesById(forumDTO.getId()),
            forumDTO.getId() == null ? null : forumRepository.findById(forumDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(Forum.class, forumDTO.getId())).getTopics()
        );
    }
}
