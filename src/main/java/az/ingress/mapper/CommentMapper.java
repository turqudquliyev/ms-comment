package az.ingress.mapper;

import az.ingress.dao.entity.CommentEntity;
import az.ingress.model.request.CreateCommentRequest;
import az.ingress.model.response.CommentResponse;
import az.ingress.model.response.PageableCommentResponse;
import org.springframework.data.domain.Page;

public enum CommentMapper {
    COMMENT_MAPPER;

    public PageableCommentResponse buildPageableCommentResponse(Page<CommentEntity> comments) {
        return PageableCommentResponse.builder()
                .comments(comments.getContent().stream().map(this::mapEntityToResponse).toList())
                .currentPage(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalItems(comments.getTotalElements())
                .build();
    }

    public CommentEntity mapRequestToEntity(Long userId,
                                            CreateCommentRequest request) {
        return CommentEntity.builder()
                .userId(userId)
                .productId(request.getProductId())
                .message(request.getMessage())
                .build();
    }

    public CommentResponse mapEntityToResponse(CommentEntity entity) {
        return CommentResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .productId(entity.getProductId())
                .message(entity.getMessage())
                .build();
    }
}