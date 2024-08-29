package odimash.openforum.infrastructure.database.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ForumDTO {

    private Long id;
    private String name;
    private Long parentForumId;

}
