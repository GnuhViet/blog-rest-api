package com.example.app.api;

import com.example.app.api.helper.PaginationHelper;
import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.api.model.authentication.AuthenticationResponse;
import com.example.app.api.model.authentication.RegisteredRequest;
import com.example.app.api.model.paging.PagedResponse;
import com.example.app.api.model.paging.PaginationRequest;
import com.example.app.dto.article.DetailsArticleDTO;
import com.example.app.service.ArticleService;
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
    public ResponseEntity<PagedResponse<DetailsArticleDTO>> getAll(@Valid PaginationRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        articleService.getAllArticle(PaginationHelper.parsePagingRequest(request), DetailsArticleDTO.class),
                        articleService.countArticle(),
                        httpServletRequest.getRequestURI()
                )
        );
    }

    // can phai dang nhap
    @PostMapping
    public ResponseEntity<DetailsArticleDTO> register(@Valid @RequestBody PostArticleRequest request, Principal principal) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articleService.createNew(request, principal.getName()));
    }

}
