package odimash.openforum.infrastructure.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicDTO {
	private Long id;
	private String name;
	private Long forumId;
	private String content;
	private Long authorId;
}