package odimash.openforum.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import odimash.openforum.infrastructure.database.dto.TopicDTO;
import odimash.openforum.infrastructure.viewdata.TopicViewData;
import odimash.openforum.service.TopicService;

@RestController()
@RequestMapping("topic")
public class TopicController {

	@Autowired
	TopicService topicService;

	@GetMapping("/{id}")
	public ResponseEntity<TopicViewData> getTopicById(@PathVariable Long id) {
		return ResponseEntity.ok(topicService.getTopicViewData(id));
	}

	@PostMapping
	public ResponseEntity<TopicViewData> createTopic(@RequestBody TopicDTO topicDTO) {
		TopicDTO createdTopicDTO = topicService.createTopic(topicDTO);
		return ResponseEntity.ok(topicService.getTopicViewData(createdTopicDTO.getId()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TopicViewData> updateTopic(@PathVariable Long id, @RequestBody TopicDTO topicDTO) {
		topicService.updateTopic(topicDTO);
		return ResponseEntity.ok(topicService.getTopicViewData(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
		topicService.deleteTopic(id);
		return ResponseEntity.noContent().build();
	}

}
