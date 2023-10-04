package com.example.app.service;

import com.example.app.api.model.article.DetailsArticleResponse;
import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.dtos.appuser.SimpleAppUserDTO;
import com.example.app.dtos.article.DetailsArticleDTO;
import com.example.app.dtos.article.PagedDetailsArticleDTO;
import com.example.app.dtos.category.CategoryDTO;
import com.example.app.entities.Article;
import com.example.app.entities.Category;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private List<DetailsArticleDTO> articleToDetailsArticleDTOList(List<Article> articles) {
        return articles.stream()
                .map(article -> {
                    DetailsArticleDTO dto = modelMapper.map(article, DetailsArticleDTO.class);
                    dto.setAuthorId(article.getAppUser().getId());
                    dto.setCategoryIds(article.getCategories()
                            .stream()
                            .map(Category::getId)
                            .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Article getReferenceById(String id) {
        return articleRepository.getReferenceById(id);
    }

    public boolean existById(String id) {
        return articleRepository.existsById(id);
    }

    public <T> List<T> getPagedArticle(Pageable page, Class<T> dtoType) {
        return articleRepository.findAll(page).getContent()
                .stream()
                .map(obj -> modelMapper.map(obj, dtoType))
                .collect(Collectors.toList());
    }

    public List<DetailsArticleDTO> getPagedArticle(Pageable page) {
        return articleToDetailsArticleDTOList(articleRepository.findAll(page).getContent());
    }

    public PagedDetailsArticleDTO searchArticleByTitle(String title, String categoryId, Pageable page) {
        boolean findByCategory = title == null && categoryId != null;
        boolean findByTitle = title != null && categoryId == null;
        boolean findByTitleAndCategory = title != null && categoryId != null;

        if (findByCategory) {
            Page<Article> res = articleRepository.findArticleByCategory(categoryId, page);
            return PagedDetailsArticleDTO.builder()
                    .detailsArticleDTOs(articleToDetailsArticleDTOList(res.getContent()))
                    .totalRecords(res.getTotalElements())
                    .build();
        }

        if (findByTitle) {
            Page<Article> res = articleRepository.findArticleByTitle(title.toLowerCase(Locale.ROOT), page);
            return PagedDetailsArticleDTO.builder()
                    .detailsArticleDTOs(articleToDetailsArticleDTOList(res.getContent()))
                    .totalRecords(res.getTotalElements())
                    .build();
        }

        if (findByTitleAndCategory) {
            Page<Article> res = articleRepository.findArticleByTitleAndCategory(
                    title.toLowerCase(Locale.ROOT),
                    categoryId,
                    page
            );
            return PagedDetailsArticleDTO.builder()
                    .detailsArticleDTOs(articleToDetailsArticleDTOList(res.getContent()))
                    .totalRecords(res.getTotalElements())
                    .build();
        }

        return PagedDetailsArticleDTO.builder()
                .detailsArticleDTOs(getPagedArticle(page))
                .totalRecords(countArticle())
                .build();
    }

    public long countArticle() {
        return articleRepository.count();
    }

    public void addCategoriesToArticle(Article article, List<String> categoryIds) {
        article.setCategories(new HashSet<>());
        categoryIds.stream()
                .distinct()
                .map(categoryService::findById)
                .forEach(category -> article.getCategories().add(category));
    }

    public DetailsArticleDTO createNew(PostArticleRequest request, String userId) {

        Article article = modelMapper.map(request, Article.class);
        article.setCreateDate(new Date(System.currentTimeMillis()));
        article.setAppUser(userService.getReferenceById(userId));
        addCategoriesToArticle(article, request.getCategoryIds());

        Article savedArticle = articleRepository.save(article);

        DetailsArticleDTO response = modelMapper.map(savedArticle, DetailsArticleDTO.class);
        response.setCategoryIds(savedArticle.getCategories().stream().map(Category::getId).collect(Collectors.toList()));
        response.setAuthorId(savedArticle.getAppUser().getId());

        return response;
    }

    public DetailsArticleResponse findById(String id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("Article id not found"));

        DetailsArticleResponse res = modelMapper.map(article, DetailsArticleResponse.class);
        res.setAuthor(modelMapper.map(article.getAppUser(), SimpleAppUserDTO.class));
        res.setCategoryDTOList(article.getCategories()
                .stream().map(c -> modelMapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList())
        );
        return res;
    }

    public DetailsArticleResponse update(String id, PostArticleRequest request, String appUserId) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("Article id not found"));
        if (!appUserId.equals(article.getAppUser().getId())) {
            throw new BadCredentialsException("access denied");
        }

        modelMapper.map(request, article);
        addCategoriesToArticle(article, request.getCategoryIds());
        article.setModifiedDate(new Date(System.currentTimeMillis()));

        DetailsArticleResponse res = modelMapper.map(article, DetailsArticleResponse.class);
        res.setAuthor(modelMapper.map(article.getAppUser(), SimpleAppUserDTO.class));
        res.setCategoryDTOList(article.getCategories()
                .stream().map(c -> modelMapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList())
        );
        return res;
    }

    public void delete(String id, String appUserId) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("Article id not found"));
        if (!appUserId.equals(article.getAppUser().getId())) {
            throw new BadCredentialsException("access denied");
        }

        articleRepository.deleteById(id);
    }
}
