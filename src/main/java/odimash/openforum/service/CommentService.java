package odimash.openforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.repository.CommentRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.CommentDTO;
import odimash.openforum.infrastructure.database.mapper.CommentMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Comment.class, id));
        return commentMapper.mapToDTO(comment);
    }

    public Set<CommentDTO> getCommentsByTopicId(Long topicId) {
        Set<Comment> comments = commentRepository.findByTopicId(topicId);
        return comments.stream().map(commentMapper::mapToDTO).collect(Collectors.toSet());
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = commentMapper.mapToEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.mapToDTO(savedComment);
    }

	public CommentDTO editComment(Long id, String content) {
		Comment comment = commentRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundByIdException(Comment.class, id));
		comment.setContent(content);
		Comment updatedComment = commentRepository.save(comment);
		return commentMapper.mapToDTO(updatedComment);
	}

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundByIdException(Comment.class, id);
        }
        commentRepository.deleteById(id);
    }
}
