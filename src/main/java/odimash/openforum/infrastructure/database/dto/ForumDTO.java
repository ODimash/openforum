package odimash.openforum.infrastructure.database.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.*;
import odimash.openforum.service.ForumService;

@Data
@AllArgsConstructor
// @NoArgsConstructor

public class ForumDTO {
    private static final Logger logger = LoggerFactory.getLogger(ForumService.class);

    private Long id;
    private String name;
    private Long parentForumId;

    public ForumDTO () {
        logger.atInfo().log("ForumDTO was created");
    }
}
