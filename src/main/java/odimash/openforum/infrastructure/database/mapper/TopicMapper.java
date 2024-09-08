package odimash.openforum.infrastructure.database.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odimash.openforum.domain.entity.Forum;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.CommentRepository;
import odimash.openforum.domain.repository.ForumRepository;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.TopicDTO;

@Component
public class TopicMapper {

	@Autowired
	private ForumRepository forumRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;


	public TopicDTO mapToDTO(Topic topic) {
		return topic == null ? null : new TopicDTO(
			topic.getId(),
			topic.getTitle(),
			topic.getForum() == null ? null : topic.getForum().getId(),
			topic.getContent(),
			topic.getAuthor() == null ? null : topic.getAuthor().getId()
		);
	}

	public Topic mapToEntity(TopicDTO topicDTO) {
		return topicDTO == null ? null : new Topic(
			topicDTO.getId(),
			topicDTO.getTitle(),

			topicDTO.getForumId() == null ? null :
				forumRepository.findById(topicDTO.getForumId())
				.orElseThrow(() -> new EntityNotFoundByIdException(Forum.class, topicDTO.getAuthorId())				),

			topicDTO.getContent(),

			topicDTO.getAuthorId() == null ? null :
				userRepository.findById(topicDTO.getAuthorId())
				.orElseThrow(() -> new EntityNotFoundByIdException(User.class, topicDTO.getAuthorId())),

			commentRepository.findByTopicId(topicDTO.getId())
		);
	}
}
