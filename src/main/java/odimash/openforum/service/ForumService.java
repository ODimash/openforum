package odimash.openforum.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.ForumDTO;
import odimash.openforum.infrastructure.database.mapper.ForumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ForumService {

    private static final Logger logger = LoggerFactory.getLogger(ForumService.class);

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ForumMapper forumMapper;

    public ForumDTO createForum(ForumDTO forumDTO) {
        logger.info("Creating forum with name: {}", forumDTO.getName());
        if (forumRepository.findByNameAndParentForumId(forumDTO.getName(), forumDTO.getParentForumId()).isPresent()) {
            logger.error("Forum with name '{}' already exists", forumDTO.getName());
            throw new EntityNameIsAlreadyTakenException(Forum.class, forumDTO.getName());
        }
        Forum savedForum = forumRepository.save(forumMapper.mapToEntity(forumDTO));
        logger.info("Forum created with ID: {}", savedForum.getId());
        return forumMapper.mapToDTO(savedForum);
    }

    public ForumDTO readForumById(Long id) {
        logger.info("Reading forum with ID: {}", id);
        return forumRepository.findById(id)
                .map(forumMapper::mapToDTO)
                .orElseThrow(() -> {
                    logger.error("Forum with ID '{}' not found", id);
                    return new EntityNotFoundByIdException(Forum.class, id);
                });
    }

    public ForumDTO updateForum(ForumDTO updateForumDTO) {
        if (updateForumDTO.getId() == null) {
            logger.error("Forum ID cannot be null for update");
            throw new IllegalArgumentException("Forum ID cannot be null for update");
        }
        if (forumRepository.findById(updateForumDTO.getId()).isEmpty()) {
            logger.error("Forum with ID '{}' not found", updateForumDTO.getId());
            throw new EntityNotFoundByIdException(Forum.class, updateForumDTO.getId());
        }
        Forum savedForum = forumRepository.save(forumMapper.mapToEntity(updateForumDTO));
        logger.info("Forum updated with ID: {}", savedForum.getId());
        return forumMapper.mapToDTO(savedForum);
    }

    public void deleteForum(Long id) {
        if (id == null) {
            logger.error("Forum ID cannot be null for deleting");
            throw new IllegalArgumentException("Forum ID cannot be null for deleting");
        }
        logger.info("Deleting forum with ID: {}", id);
        forumRepository.deleteById(id);
    }

    public List<String> getPathAsString(Long currentForumId) {
        List<String> path = new ArrayList<>();
        getPathAsDTO(currentForumId).forEach(forumDTO -> path.add(forumDTO.getName()));
        return path;
    }

    public List<ForumDTO> getPathAsDTO(Long currentForumId) {
        logger.info("Getting path as DTO for forum with ID: {}", currentForumId);
        List<ForumDTO> path = new ArrayList<>();
        Forum currentForum = forumRepository.findById(currentForumId)
                .orElseThrow(() -> {
                    logger.error("Forum with ID '{}' not found", currentForumId);
                    return new EntityNotFoundByIdException(Forum.class, currentForumId);
                });
        while (currentForum != null) {
            path.add(forumMapper.mapToDTO(currentForum));
            currentForum = currentForum.getParentForum();
        }
        Collections.reverse(path);
        return path;
    }
}
