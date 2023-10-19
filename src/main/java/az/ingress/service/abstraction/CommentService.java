package az.ingress.service.abstraction;

import az.ingress.model.request.CreateCommentRequest;
import az.ingress.model.request.UpdateCommentRequest;
import az.ingress.model.response.CommentResponse;
import az.ingress.model.response.PageableCommentResponse;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    void createComment(Long userId, CreateCommentRequest request);

    CommentResponse getCommentById(Long id);

    PageableCommentResponse getAllCommentByProductId(Long productId, Integer pageNumber, Integer pageSize);

    void updateCommentById(Long id, UpdateCommentRequest request);

    void deleteCommentById(Long id);
}