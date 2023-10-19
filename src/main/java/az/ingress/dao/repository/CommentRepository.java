package az.ingress.dao.repository;

import az.ingress.dao.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllCommentByProductIdOrderByCreatedAtDesc(Long productId,
                                                                      Pageable pageable);
}