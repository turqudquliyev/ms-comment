package az.ingress.controller

import az.ingress.exception.GlobalExceptionHandler
import az.ingress.model.request.CreateCommentRequest
import az.ingress.model.request.UpdateCommentRequest
import az.ingress.model.response.CommentResponse
import az.ingress.service.abstraction.CommentService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static az.ingress.model.constant.HeaderConstant.USER_ID
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class CommentControllerTest extends Specification {
    CommentService commentService
    CommentController commentController
    MockMvc mockMvc

    def setup() {
        commentService = Mock()
        commentController = new CommentController(commentService)
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build()
    }

    def "TestCreateComment success case"() {
        given:
        def userId = 2L
        def url = "/v1/comments"
        def createCommentRequest = new CreateCommentRequest(
                3L, "message"
        )
        def jsonRequest = '''
                                        {
                                          "productId": 3,
                                          "message": "message"
                                        }
                                 '''

        when:
        def jsonResponse = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .header(USER_ID, userId as String)
                .content(jsonRequest)
        ).andReturn()

        then:
        1 * commentService.createComment(userId, createCommentRequest)
        jsonResponse.response.status == CREATED.value()
    }

    def "TestGetCommentById success case"() {
        given:
        def id = 1L
        def userId = 2L
        def url = "/v1/comments/$id"
        def specificDateTime = LocalDateTime.of(2023, 10, 21, 12, 30)
        def commentResponse = new CommentResponse(
                1L, 2L, 3L, "message", specificDateTime
        )
        def expectedJsonResponse = '''
                                            {
                                              "id": 1,
                                              "userId": 2,
                                              "productId": 3,
                                              "message": "message",
                                              "createdAt": "21.10.2023 12:30"
                                            }
                                      '''

        when:
        def jsonResponse = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
                .header(USER_ID, userId as String)
        ).andReturn()

        then:
        1 * commentService.getCommentById(id) >> commentResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(jsonResponse.response.contentAsString.toString(), expectedJsonResponse.toString(), true)
    }

    def "TestGetAllCommentByProductId success case"() {
        given:
        def productId = 3L
        def url = "/v1/comments"
        def specificDateTime = LocalDateTime.of(2023, 10, 21, 12, 30)
        def commentResponse = new CommentResponse(
                1L, 2L, 3L, "message", specificDateTime
        )
        def expectedJsonResponse = '''
                                            [
                                            {
                                              "id": 1,
                                              "userId": 2,
                                              "productId": 3,
                                              "message": "message",
                                              "createdAt": "21.10.2023 12:30"
                                            }
                                            ]
                                      '''

        when:
        def jsonResponse = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
                .param("productId", productId as String)
        ).andReturn()

        then:
        1 * commentService.getAllCommentByProductId(productId) >> [commentResponse]
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(jsonResponse.response.contentAsString.toString(), expectedJsonResponse.toString(), true)
    }

    def "TestUpdateCommentById success case"() {
        given:
        def id = 1L
        def url = "/v1/comments/$id"
        def updateCommentRequest = new UpdateCommentRequest("message")
        def jsonRequest = '''
                                        {
                                          "message": "message"
                                        }                                 
                                 '''

        when:
        def jsonResponse = mockMvc.perform(put(url)
                .contentType(APPLICATION_JSON)
                .content(jsonRequest)
        ).andReturn()

        then:
        1 * commentService.updateCommentById(id, updateCommentRequest)
        jsonResponse.response.status == NO_CONTENT.value()
    }

    def "TestDeleteCommentById success case"() {
        def id = 1L
        def url = "/v1/comments/$id"

        when:
        def jsonResponse = mockMvc.perform(delete(url)
                .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        jsonResponse.response.status == NO_CONTENT.value()
    }
}