package odimash.openforum.infrastructure.database.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.entity.Topic;
import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.TopicRepository;
import odimash.openforum.domain.repository.UserRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.CommentDTO;

@Component
public class CommentMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    public CommentDTO mapToDTO(Comment comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getAuthor() == null ? null : comment.getAuthor().getId(),
            comment.getTopic() == null ? null : comment.getTopic().getId(),
            comment.getContent()
        );
    }

    public Comment mapToEntity(CommentDTO commentDTO) {
        return new Comment(
            commentDTO.getId(),
            commentDTO.getAuthorId() == null ? null : userRepository.findById(commentDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundByIdException(User.class, commentDTO.getAuthorId())),
			commentDTO.getTopicId() == null ? null : topicRepository.findById(commentDTO.getTopicId())
                .orElseThrow(() -> new EntityNotFoundByIdException(Topic.class, commentDTO.getTopicId())),
            commentDTO.getContent()
        );
    }
}
