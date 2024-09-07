package odimash.openforum.infrastructure.database.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odimash.openforum.domain.entity.Rights;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
	private Long id;
	private String name;
	private Long controlledForumId;
	private Set<Rights> rights;
}
