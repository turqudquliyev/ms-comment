package az.ingress.service.concrete;

import az.ingress.client.ProductClient;
import az.ingress.dao.entity.CommentEntity;
import az.ingress.dao.repository.CommentRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.CreateCommentRequest;
import az.ingress.model.request.UpdateCommentRequest;
import az.ingress.model.response.CommentResponse;
import az.ingress.service.abstraction.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.ingress.mapper.CommentMapper.COMMENT_MAPPER;
import static az.ingress.model.enums.CommentStatus.DELETED;
import static az.ingress.model.enums.CommentStatus.UPDATED;
import static az.ingress.model.enums.ExceptionMessage.COMMENT_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CommentServiceHandler implements CommentService {
    CommentRepository commentRepository;
    ProductClient productClient;

    public void createComment(Long userId, CreateCommentRequest request) {
        productClient.getProductIfExist(request.getProductId());
        var comment = COMMENT_MAPPER.mapRequestToEntity(userId, request);
        commentRepository.save(comment);
    }

    public CommentResponse getCommentById(Long id) {
        var comment = fetchIfExist(id);
        return COMMENT_MAPPER.mapEntityToResponse(comment);
    }

    public List<CommentResponse> getAllCommentByProductId(Long productId) {
        var comments = commentRepository.findAllCommentByProductIdOrderByCreatedAtDesc(productId);
        return comments.stream().map(COMMENT_MAPPER::mapEntityToResponse).toList();
    }


    public void updateCommentById(Long id, UpdateCommentRequest request) {
        var comment = fetchIfExist(id);
        comment.setMessage(request.getMessage());
        comment.setStatus(UPDATED);
        commentRepository.save(comment);
    }

    public void deleteCommentById(Long id) {
        var comment = fetchIfExist(id);
        comment.setStatus(DELETED);
        commentRepository.save(comment);
    }

    private CommentEntity fetchIfExist(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
    }
}