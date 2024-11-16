package odimash.openforum.infrastructure.viewdata;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import odimash.openforum.infrastructure.database.dto.ForumDTO;
import odimash.openforum.infrastructure.database.dto.TopicDTO;

@Data
@AllArgsConstructor
public class ForumViewData {
	private ForumDTO forum;
	private List<ForumDTO> subCategories;
	private List<TopicDTO> topics;
	private List<String> path;
}
