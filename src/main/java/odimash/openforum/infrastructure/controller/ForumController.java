package odimash.openforum.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import odimash.openforum.infrastructure.database.dto.ForumDTO;
import odimash.openforum.infrastructure.viewdata.ForumViewData;
import odimash.openforum.service.ForumService;

@RestController
@RequestMapping("/forum")
public class ForumController {

	@Autowired
	private ForumService forumService;

	@GetMapping("/{id}")
	public ResponseEntity<ForumViewData> getForumById(@PathVariable Long id, Model model) {
		ForumViewData forumViewData = forumService.getForumViewData(id);
		model.addAttribute("forum", forumViewData.getForum());
		model.addAttribute("subcategories", forumViewData.getSubCategories());
		model.addAttribute("topics", forumViewData.getTopics());
		return ResponseEntity.ok(forumService.getForumViewData(id));
	}

	@PostMapping()
    public ForumDTO createCategory(@RequestParam String name, @RequestParam Long parentId) {
        ForumDTO forumDTO = new ForumDTO();
        forumDTO.setName(name);
        forumDTO.setParentForumId(parentId);
        return forumService.createForum(forumDTO);
    }

	@PutMapping("/{id}")
	public ResponseEntity<ForumDTO> updateForum(@PathVariable Long id, @RequestBody ForumDTO forumDTO) {
		forumDTO.setId(id);
		ForumDTO updatedForum = forumService.updateForum(forumDTO);
		return ResponseEntity.ok(updatedForum);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteForum(@PathVariable Long id) {
		forumService.deleteForum(id);
		return ResponseEntity.noContent().build();
	}
}
