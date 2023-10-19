package az.ingress.controller;

import az.ingress.model.request.CreateCommentRequest;
import az.ingress.model.request.UpdateCommentRequest;
import az.ingress.model.response.CommentResponse;
import az.ingress.model.response.PageableCommentResponse;
import az.ingress.service.abstraction.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static az.ingress.model.constant.HeaderConstant.USER_ID;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CommentController {
    CommentService commentService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createComment(@RequestHeader(USER_ID) Long userId,
                              @RequestBody CreateCommentRequest request) {
        commentService.createComment(userId, request);
    }

    @GetMapping("/{id}")
    public CommentResponse getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @GetMapping
    public PageableCommentResponse getAllCommentByProductId(Long productId,
                                                            Integer pageNumber,
                                                            Integer pageSize) {
        return commentService.getAllCommentByProductId(productId, pageNumber, pageSize);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateCommentById(@PathVariable Long id,
                                  @RequestBody UpdateCommentRequest request) {
        commentService.updateCommentById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
    }
}