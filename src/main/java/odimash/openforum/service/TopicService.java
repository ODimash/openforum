package odimash.openforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.repository.TopicRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.TopicDTO;
import odimash.openforum.infrastructure.database.dto.UserDTO;
import odimash.openforum.infrastructure.database.mapper.TopicMapper;
import odimash.openforum.infrastructure.viewdata.CommentViewData;
import odimash.openforum.infrastructure.viewdata.TopicViewData;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Data
public class TopicService {

  private static final Logger logger = LoggerFactory.getLogger(TopicService.class);

  @Autowired
  private TopicRepository topicRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private TopicMapper topicMapper;

  public TopicDTO createTopic(TopicDTO topicDTO) {
    logger.info("Creating topic with title: {}", topicDTO.getTitle());
    Topic savedTopic = topicRepository.save(topicMapper.mapToEntity(topicDTO));
    logger.info("Topic created with ID: {}", savedTopic.getId());
    return topicMapper.mapToDTO(savedTopic);
  }

  public TopicDTO readTopic(Long id) {
    if (id == null) {
      logger.error("ID for search topic is null");
      throw new IllegalArgumentException("ID for search topic is null");
    }

    logger.info("Reading topic with ID: {}", id);
    return topicMapper.mapToDTO(
      topicRepository.findById(id)
        .orElseThrow(() -> {
          logger.error("Topic with ID '{}' not found", id);
          return new EntityNotFoundByIdException(Topic.class, id);
        })
    );
  }

  public List<TopicDTO> getTopicListByForum(Long forumId) {
    return topicRepository.findByForumId(forumId).stream()
          .map(topicMapper::mapToDTO).collect(Collectors.toList());
  }

  public TopicDTO updateTopic(TopicDTO topicDTOtoUpdate) {
    if (topicDTOtoUpdate.getId() == null)  {
      logger.error("ID from TopicDTO is null");
      throw new IllegalArgumentException("ID from TopicDTO is null");
    }

    logger.info("Updating topic with ID: {}", topicDTOtoUpdate.getId());
    if (topicRepository.findById(topicDTOtoUpdate.getId()).isEmpty()) {
      logger.error("Topic with ID '{}' not found", topicDTOtoUpdate.getId());
      throw new EntityNotFoundByIdException(Topic.class, topicDTOtoUpdate.getId());
    }

    Topic topicToUpdate = topicMapper.mapToEntity(topicDTOtoUpdate);
    Topic updatedTopic = topicRepository.save(topicToUpdate);
    logger.info("Topic updated with ID: {}", updatedTopic.getId());
    return topicMapper.mapToDTO(updatedTopic);
  }

  public void deleteTopic(Long id) {
    if (id == null) {
      logger.error("ID for delete topic cannot be null");
      throw new IllegalArgumentException("ID for delete topic cannot be null");
    }

    logger.info("Deleting topic with ID: {}", id);
    topicRepository.deleteById(id);
  }

public TopicViewData getTopicViewData(Long id) {
  TopicDTO topicDTO = this.readTopic(id);
  UserDTO authorDTO = userService.readUser(topicDTO.getAuthorId());
  List<CommentViewData> commentViewDataList = commentService.getCommentViewDataByTopicId(topicDTO.getId());
  TopicViewData topicViewData = new TopicViewData(topicDTO, authorDTO, commentViewDataList);
  return topicViewData;
}

}
