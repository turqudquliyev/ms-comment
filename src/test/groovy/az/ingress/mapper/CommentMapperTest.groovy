package az.ingress.mapper

import az.ingress.dao.entity.CommentEntity
import az.ingress.model.request.CreateCommentRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.CommentMapper.COMMENT_MAPPER
import static az.ingress.model.enums.CommentStatus.CREATED

class CommentMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestMapRequestToEntity"() {
        given:
        def userId = random.nextLong()
        def commentRequest = random.nextObject(CreateCommentRequest)

        when:
        def commentEntity = COMMENT_MAPPER.mapRequestToEntity(userId, commentRequest)
        then:
        userId == commentEntity.userId
        CREATED == commentEntity.status
        commentRequest.productId == commentEntity.productId
        commentRequest.message == commentEntity.message
    }

    def "TestMapEntityToResponse"() {
        given:
        def commentEntity = random.nextObject(CommentEntity)

        when:
        def commentResponse = COMMENT_MAPPER.mapEntityToResponse(commentEntity)

        then:
        commentEntity.id == commentResponse.id
        commentEntity.userId == commentResponse.userId
        commentEntity.productId == commentResponse.productId
        commentEntity.message == commentResponse.message
        commentEntity.createdAt == commentResponse.createdAt
    }
}