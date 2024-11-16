package odimash.openforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odimash.openforum.domain.entity.Comment;
import odimash.openforum.domain.repository.CommentRepository;
import odimash.openforum.exception.EntityNotFoundByIdException;
import odimash.openforum.infrastructure.database.dto.CommentDTO;
import odimash.openforum.infrastructure.database.mapper.CommentMapper;
import odimash.openforum.infrastructure.viewdata.CommentViewData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    public CommentDTO getCommentById(Long id) {
        logger.info("Fetching comment with ID: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Comment with ID '{}' not found", id);
                    return new EntityNotFoundByIdException(Comment.class, id);
                });
        return commentMapper.mapToDTO(comment);
    }

    public List<CommentDTO> getCommentsByTopicId(Long topicId) {
        logger.info("Fetching comments for topic ID: {}", topicId);
        Set<Comment> comments = commentRepository.findByTopicId(topicId);
        return comments.stream().map(commentMapper::mapToDTO).collect(Collectors.toList());
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        logger.info("Creating a new comment for topic ID: {}", commentDTO.getTopicId());
        Comment comment = commentMapper.mapToEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        logger.info("Created new comment with ID: {}", savedComment.getId());
        return commentMapper.mapToDTO(savedComment);
    }

    public CommentDTO editComment(Long id, String content) {
        logger.info("Editing comment with ID: {}", id);
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Comment with ID '{}' not found", id);
                return new EntityNotFoundByIdException(Comment.class, id);
            });
        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);
        logger.info("Updated comment with ID: {}", updatedComment.getId());
        return commentMapper.mapToDTO(updatedComment);
    }

    public void deleteComment(Long id) {
        logger.info("Deleting comment with ID: {}", id);
        if (!commentRepository.existsById(id)) {
            logger.error("Comment with ID '{}' not found", id);
            throw new EntityNotFoundByIdException(Comment.class, id);
        }
        commentRepository.deleteById(id);
        logger.info("Deleted comment with ID: {}", id);
    }

    public List<CommentViewData> getCommentViewDataByTopicId(Long topicId) {
        List<CommentDTO> commentDTOList = getCommentsByTopicId(topicId);
        return mapCommentDTOtoCommentViewData(commentDTOList);
    }

    private List<CommentViewData> mapCommentDTOtoCommentViewData(List<CommentDTO> commentDTOList) {
        List<CommentViewData> commentViewDataList = new ArrayList<>();
        for (var commentDTO : commentDTOList) {
            var authorDTO = userService.readUser(commentDTO.getAuthorId());
            commentViewDataList.add(new CommentViewData(commentDTO, authorDTO));
        }
        return commentViewDataList;
    }
}
