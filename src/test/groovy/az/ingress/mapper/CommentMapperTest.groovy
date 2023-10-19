package az.ingress.mapper

import az.ingress.dao.entity.CommentEntity
import az.ingress.model.request.CreateCommentRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

import static az.ingress.mapper.CommentMapper.COMMENT_MAPPER

class CommentMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestBuildPageableCommentResponse"() {
        given:
        def commentEntities = [random.nextObject(CommentEntity)]
        def pageOfComments = new PageImpl(commentEntities)

        when:
        def pageableCommentResponse = COMMENT_MAPPER.buildPageableCommentResponse(pageOfComments)

        then:
        pageOfComments.content[0].id == pageableCommentResponse.comments[0].id
        pageOfComments.content[0].userId == pageableCommentResponse.comments[0].userId
        pageOfComments.content[0].productId == pageableCommentResponse.comments[0].productId
        pageOfComments.content[0].message == pageableCommentResponse.comments[0].message
        pageOfComments.number == pageableCommentResponse.currentPage
        pageOfComments.totalPages == pageableCommentResponse.totalPages
        pageOfComments.totalElements == pageableCommentResponse.totalItems
    }

    def "TestMapRequestToEntity"() {
        given:
        def userId = random.nextLong()
        def commentRequest = random.nextObject(CreateCommentRequest)

        when:
        def commentEntity = COMMENT_MAPPER.mapRequestToEntity(userId, commentRequest)
        then:
        userId == commentEntity.userId
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
    }
}