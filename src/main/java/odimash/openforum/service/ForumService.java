package odimash.openforum.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.infrastructure.database.dto.ForumDTO;
import odimash.openforum.infrastructure.database.mapper.ForumMapper;
import odimash.openforum.service.exception.EntityDoesNotExistException;
import odimash.openforum.service.exception.EntityNameIsAlreadyTakenException;
import odimash.openforum.service.exception.EntityNotFoundByIdException;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ForumMapper forumMapper;

    public ForumDTO createForum(ForumDTO forumDTO) {

        boolean isForumAlreadyExists = forumRepository.findByNameAndParentForumId(forumDTO.getName(), forumDTO.getParentForumId()).isPresent();
        if (isForumAlreadyExists) {
            throw new EntityNameIsAlreadyTakenException(Forum.class, forumDTO.getName());
        }


        Forum savedForum = forumRepository.save(forumMapper.mapToEntity(forumDTO));
        return forumMapper.mapToDTO(savedForum);
    }

    public ForumDTO readForumById(Long id) {
        Optional<Forum> searchResult = forumRepository.findById(id);
        if (searchResult.isEmpty()) {
            throw new EntityNotFoundByIdException(Forum.class, id);
        }
        return forumMapper.mapToDTO(searchResult.get());
    }

    public ForumDTO updateForum(ForumDTO updateForumDTO) {
        if (updateForumDTO.getId() == null) {
            throw new EntityDoesNotExistException(Forum.class, updateForumDTO.getName());
        }

        if (forumRepository.findById(updateForumDTO.getId()).isEmpty()) {
            throw new EntityNotFoundByIdException(Forum.class, updateForumDTO.getId());
        }

        Forum savedForum = forumRepository.save(forumMapper.mapToEntity(updateForumDTO));
        return forumMapper.mapToDTO(savedForum);
    }

    public void deleteForum(Long id) {

        try {
            forumRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public List<String> getPathAString(Long currentForumId) {

        List<String> path = new ArrayList<String>();
        List<ForumDTO> pathAsDTO;

        try {
            pathAsDTO = getPathAsDTO(currentForumId);
        } catch (EntityNotFoundByIdException e) {
            throw e;
        }

        for (ForumDTO forumDTO : pathAsDTO) {
            path.add(forumDTO.getName());
        }

        return path;
    }

    public List<ForumDTO> getPathAsDTO(Long currentForumId) {

        List<ForumDTO> path = new ArrayList<ForumDTO>();

        Optional<Forum> searchResult = forumRepository.findById(currentForumId);
        if (searchResult.isEmpty()) {
            throw new EntityNotFoundByIdException(Forum.class, currentForumId);
        }

        Forum currentForum = searchResult.get();
        while (currentForum != null) {
            path.add(forumMapper.mapToDTO(currentForum));
            currentForum = currentForum.getParentForum();
        }

        Collections.reverse(path);
        return path;

    }

}
