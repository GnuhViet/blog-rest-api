package com.example.app.service;

import com.example.app.api.model.article.DetailsArticleResponse;
import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.dto.appuser.SimpleAppUserDTO;
import com.example.app.dto.article.DetailsArticleDTO;
import com.example.app.dto.category.CategoryDTO;
import com.example.app.entities.Article;
import com.example.app.entities.Category;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.sql.Date;
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

    public <T> List<T> getPagedArticle(Pageable page, Class<T> dtoType) {
        return articleRepository.findAll(page).getContent()
                .stream()
                .map(obj -> modelMapper.map(obj, dtoType))
                .collect(Collectors.toList());
    }

    public List<DetailsArticleDTO> getPagedArticle(Pageable page) {
        return articleRepository.findAll(page).getContent()
                .stream()
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

    public List<DetailsArticleDTO> searchArticleByTitle(String title, Pageable page) {
        if (title == null || title.isEmpty()) {
            return getPagedArticle(page);
        }

        return articleRepository.findArticleByTitle(title.toLowerCase(Locale.ROOT), page).getContent()
                .stream()
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

    public DetailsArticleResponse update(String id, PostArticleRequest request, String appUserId)
    {
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
