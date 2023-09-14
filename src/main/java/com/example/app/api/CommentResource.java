package com.example.app.api;

import com.example.app.api.model.comment.CreateCommentRequest;
import com.example.app.dtos.comment.CommentDTO;
import com.example.app.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentResource {
    private final CommentService commentService;

    @GetMapping("/{articleId}")
    @Operation(summary = "Get article comment, Role: All")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable String articleId) {
        return ResponseEntity.ok(
                commentService.getCommentsByArticleId(articleId)
        );
    }

    @PostMapping
    @Operation(summary = "Create new comment, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CommentDTO> createNewComment(CreateCommentRequest request, Principal principal) {
        return ResponseEntity.ok(
                commentService.createNew(request, principal.getName())
        );
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Create new comment, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CommentDTO> updateComment(@PathVariable String commentId, CreateCommentRequest request, Principal principal) {
        return ResponseEntity.ok(
                commentService.updateComment(commentId,request, principal.getName())
        );
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Create new comment, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> createNewComment(@PathVariable String commentId, Principal principal) {
        commentService.delete(commentId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
