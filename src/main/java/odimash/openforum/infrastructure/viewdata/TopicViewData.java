package odimash.openforum.infrastructure.viewdata;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import odimash.openforum.infrastructure.database.dto.TopicDTO;
import odimash.openforum.infrastructure.database.dto.UserDTO;


@Data
@AllArgsConstructor
public class TopicViewData {
    private TopicDTO topic;
    private UserDTO author;
    private List<CommentViewData> comments;
}
