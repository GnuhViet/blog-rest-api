package com.example.app.api;

import com.example.app.api.helper.PaginationHelper;
import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.api.model.authentication.AuthenticationResponse;
import com.example.app.api.model.authentication.RegisteredRequest;
import com.example.app.api.model.paging.PagedResponse;
import com.example.app.api.model.paging.PaginationRequest;
import com.example.app.dto.article.DetailsArticleDTO;
import com.example.app.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<PagedResponse<DetailsArticleDTO>> getPaging(@Valid PaginationRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        articleService.getAllArticle(PaginationHelper.parsePagingRequest(request)),
                        articleService.countArticle(),
                        httpServletRequest.getRequestURI()
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

}
