package az.ingress.service

import az.ingress.client.ProductClient
import az.ingress.client.mock.MockProductClient
import az.ingress.dao.entity.CommentEntity
import az.ingress.dao.repository.CommentRepository
import az.ingress.exception.NotFoundException
import az.ingress.model.request.CreateCommentRequest
import az.ingress.model.request.UpdateCommentRequest
import az.ingress.model.response.CommentResponse
import az.ingress.service.abstraction.CommentService
import az.ingress.service.concrete.CommentServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDateTime

class CommentServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    CommentRepository commentRepository
    ProductClient productClient
    CommentService commentService

    def setup() {
        commentRepository = Mock()
        productClient = new MockProductClient()
        commentService = new CommentServiceHandler(commentRepository, productClient)
    }

    def "TestCreateComment success case"() {
        given:
        def userId = random.nextLong()
        def createCommentRequest = random.nextObject(CreateCommentRequest)
        def commentEntity = random.nextObject(CommentEntity)

        when:
        commentService.createComment(userId, createCommentRequest)

        then:
        1 * productClient.checkStockAvailability(createCommentRequest.productId)
        1 * commentRepository.save(commentEntity) >> commentEntity
        createCommentRequest.productId == commentEntity.productId
        createCommentRequest.message == commentEntity.message
    }

    def "TestGetCommentById success case"() {
        given:
        def id = random.nextLong()
        def commentEntity = random.nextObject(CommentEntity)

        when:
        def commentResponse = commentService.getCommentById(id)

        then:
        1 * commentRepository.findById(id) >> Optional.of(commentEntity)
        commentEntity.id == commentResponse.id
        commentEntity.userId == commentResponse.userId
        commentEntity.productId == commentResponse.productId
        commentEntity.message == commentResponse.message
    }

    def "TestGetCommentById CommentNotFound case"() {
        given:
        def id = random.nextLong()

        when:
        commentService.getCommentById(id)

        then:
        1 * commentRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "COMMENT_NOT_FOUND"
    }

    def "TestGetAllCommentByProductId full response case"() {
        given:
        def productId = 1L
        def pageable = PageRequest.of(0, 10)
        def comments = [new CommentEntity(1L, 2L, 3L, "message", LocalDateTime.now(), LocalDateTime.now())]
        def commentResponses = [new CommentResponse(1L, 2L, 3L, "message")]

        when:
        def pageableCommentResponse = commentService.getAllCommentByProductId(productId, pageable.pageNumber, pageable.pageSize)

        then:
        1 * commentRepository.findAllCommentByProductIdOrderByCreatedAtDesc(productId, pageable) >> new PageImpl<>(comments, pageable, 10)
        pageableCommentResponse.comments == commentResponses
        pageableCommentResponse.currentPage == 0
        pageableCommentResponse.totalPages == 1
        pageableCommentResponse.totalItems == 10
    }

    def "TestGetAllCommentByProductId empty response case"() {
        given:
        def productId = 1L
        def pageable = PageRequest.of(0, 10)
        def comments = []
        def commentResponses = []

        when:
        def pageableCommentResponse = commentService.getAllCommentByProductId(productId, pageable.pageNumber, pageable.pageSize)

        then:
        1 * commentRepository.findAllCommentByProductIdOrderByCreatedAtDesc(productId, pageable) >> new PageImpl<>(comments, pageable, 0)
        pageableCommentResponse.comments == commentResponses
        pageableCommentResponse.currentPage == 0
        pageableCommentResponse.totalPages == 0
        pageableCommentResponse.totalItems == 0
    }

    def "TestUpdateCommentById success case"() {
        given:
        def id = random.nextLong()
        def updateCommentRequest = random.nextObject(UpdateCommentRequest)
        def commentEntity = random.nextObject(CommentEntity)

        when:
        commentService.updateCommentById(id, updateCommentRequest)

        then:
        1 * commentRepository.findById(id) >> Optional.of(commentEntity)
        commentEntity.message == updateCommentRequest.message
        1 * commentRepository.save(commentEntity)
    }

    def "TestUpdateCommentById CommentNotFound case"() {
        given:
        def id = random.nextLong()
        def updateCommentRequest = random.nextObject(UpdateCommentRequest)

        when:
        commentService.updateCommentById(id, updateCommentRequest)

        then:
        1 * commentRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "COMMENT_NOT_FOUND"
    }

    def "TestDeleteCommentById success case"() {
        given:
        def id = random.nextLong()
        def commentEntity = random.nextObject(CommentEntity)

        when:
        commentService.deleteCommentById(id)

        then:
        1 * commentRepository.findById(id) >> Optional.of(commentEntity)
        1 * commentRepository.delete(commentEntity)
    }

    def "TestDeleteCommentById CommentNotFound case"() {
        given:
        def id = random.nextLong()

        when:
        commentService.deleteCommentById(id)

        then:
        1 * commentRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "COMMENT_NOT_FOUND"
    }
}