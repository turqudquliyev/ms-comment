package az.ingress.service.concrete;

import az.ingress.client.ProductClient;
import az.ingress.dao.entity.CommentEntity;
import az.ingress.dao.repository.CommentRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.CreateCommentRequest;
import az.ingress.model.request.UpdateCommentRequest;
import az.ingress.model.response.CommentResponse;
import az.ingress.model.response.PageableCommentResponse;
import az.ingress.service.abstraction.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static az.ingress.mapper.CommentMapper.COMMENT_MAPPER;
import static az.ingress.model.enums.ExceptionMessage.COMMENT_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CommentServiceHandler implements CommentService {
    CommentRepository commentRepository;
    ProductClient productClient;

    public void createComment(Long userId, CreateCommentRequest request) {
        productClient.checkStockAvailability(request.getProductId());
        CommentEntity comment = COMMENT_MAPPER.mapRequestToEntity(userId, request);
        commentRepository.save(comment);
    }

    public CommentResponse getCommentById(Long id) {
        CommentEntity comment = fetchIfExist(id);
        return COMMENT_MAPPER.mapEntityToResponse(comment);
    }

    public PageableCommentResponse getAllCommentByProductId(Long productId, Integer pageNumber, Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentEntity> comments = commentRepository.findAllCommentByProductIdOrderByCreatedAtDesc(productId, pageable);
        return COMMENT_MAPPER.buildPageableCommentResponse(comments);
    }

    public void updateCommentById(Long id, UpdateCommentRequest request) {
        CommentEntity comment = fetchIfExist(id);
        comment.setMessage(request.getMessage());
        commentRepository.save(comment);
    }

    public void deleteCommentById(Long id) {
        CommentEntity comment = fetchIfExist(id);
        commentRepository.delete(comment);
    }

    private CommentEntity fetchIfExist(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
    }
}