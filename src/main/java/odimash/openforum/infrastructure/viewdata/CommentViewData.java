package odimash.openforum.infrastructure.viewdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import odimash.openforum.infrastructure.database.dto.CommentDTO;
import odimash.openforum.infrastructure.database.dto.UserDTO;

@Data
@AllArgsConstructor
public class CommentViewData {
    private CommentDTO commentDTO;
    private UserDTO authorName;
}
