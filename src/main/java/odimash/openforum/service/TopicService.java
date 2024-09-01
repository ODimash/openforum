package odimash.openforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.repository.TopicRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.TopicDTO;
import odimash.openforum.infrastructure.database.mapper.TopicMapper;

@Service
@Data
public class TopicService {

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private TopicMapper topicMapper;

	public TopicDTO createTopic(TopicDTO topicDTO) {
		Topic savedTopic = topicRepository.save(topicMapper.mapToEntity(topicDTO));
		return topicMapper.mapToDTO(savedTopic);
	}

	public TopicDTO readTopic(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("ID for search topic is null");
		}

		return topicMapper.mapToDTO(
			topicRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundByIdException(Topic.class, id))
		);
	}

	public TopicDTO updateTopic(TopicDTO topicDTOtoUpdate) {
		if (topicDTOtoUpdate.getId() == null)  {
			throw new IllegalArgumentException("ID from TopicDTO is null");
		}

		if (topicRepository.findById(topicDTOtoUpdate.getId()).isEmpty()) {
			throw new EntityNotFoundByIdException(Topic.class, topicDTOtoUpdate.getId());
		}

		Topic topicToUpdate = topicMapper.mapToEntity(topicDTOtoUpdate);
		return topicMapper.mapToDTO(topicRepository.save(topicToUpdate));
	}

	public void deleteTopic(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("ID for delete topic can not be null");
		}

		topicRepository.deleteById(id);
	}

}
