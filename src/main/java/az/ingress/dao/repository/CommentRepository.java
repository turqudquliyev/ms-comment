package az.ingress.dao.repository;

import az.ingress.dao.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    List<CommentEntity> findAllCommentByProductIdOrderByCreatedAtDesc(Long productId);
}