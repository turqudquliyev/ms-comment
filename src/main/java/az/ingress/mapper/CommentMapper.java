package az.ingress.mapper;

import az.ingress.dao.entity.CommentEntity;
import az.ingress.model.request.CreateCommentRequest;
import az.ingress.model.response.CommentResponse;

import static az.ingress.model.enums.CommentStatus.CREATED;

public enum CommentMapper {
    COMMENT_MAPPER;

    public CommentEntity mapRequestToEntity(Long userId,
                                            CreateCommentRequest request) {
        return CommentEntity.builder()
                .userId(userId)
                .productId(request.getProductId())
                .message(request.getMessage())
                .status(CREATED)
                .build();
    }

    public CommentResponse mapEntityToResponse(CommentEntity entity) {
        return CommentResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .productId(entity.getProductId())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}