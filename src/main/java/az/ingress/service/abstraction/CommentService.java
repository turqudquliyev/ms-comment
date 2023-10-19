package az.ingress.service.abstraction;

import az.ingress.model.request.CreateCommentRequest;
import az.ingress.model.request.UpdateCommentRequest;
import az.ingress.model.response.CommentResponse;

import java.util.List;

public interface CommentService {
    void createComment(Long userId, CreateCommentRequest request);

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getAllCommentByProductId(Long productId);

    void updateCommentById(Long id, UpdateCommentRequest request);

    void deleteCommentById(Long id);
}