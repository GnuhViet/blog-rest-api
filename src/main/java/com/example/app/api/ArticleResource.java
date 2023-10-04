package com.example.app.api;

import com.example.app.api.helper.PaginationHelper;
import com.example.app.api.model.article.DetailsArticleResponse;
import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.api.model.paging.PagedResponse;
import com.example.app.api.model.paging.PaginationRequest;
import com.example.app.dtos.article.DetailsArticleDTO;
import com.example.app.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/article")
public class ArticleResource {
    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "Get article(home page)")
    public ResponseEntity<PagedResponse<DetailsArticleDTO>> getPaging(@Valid PaginationRequest request) {
        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        articleService.getPagedArticle(PaginationHelper.parsePagingRequest(request)),
                        articleService.countArticle()
                )
        );
    }

    // can phai dang nhap
    @PostMapping
    @Operation(summary = "Create new articles, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DetailsArticleDTO> create(@Valid @RequestBody PostArticleRequest request, Principal principal) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articleService.createNew(request, principal.getName()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search articles, Role: All")
    public ResponseEntity<PagedResponse<DetailsArticleDTO>> searchPaging(
            @Valid PaginationRequest request,
            @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        articleService.searchArticleByTitle(title, PaginationHelper.parsePagingRequest(request)),
                        articleService.countArticle()
                )
        );
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "Article page, Role: All")
    public ResponseEntity<DetailsArticleResponse> articleDetails(@PathVariable String articleId) {
        return ResponseEntity.ok(
            articleService.findById(articleId)
        );
    }

    @PutMapping("/{articleId}")
    @Operation(summary = "Edit article, Role: all", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DetailsArticleResponse> articleEdit(
            @PathVariable String articleId,
            @Valid @RequestBody PostArticleRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(
                articleService.update(articleId, request, principal.getName())
        );
    }

    @DeleteMapping("/{articleId}")
    @Operation(summary = "Delete article, Role: all", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> articleDelete(@PathVariable String articleId, Principal principal) {
        articleService.delete(articleId, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
