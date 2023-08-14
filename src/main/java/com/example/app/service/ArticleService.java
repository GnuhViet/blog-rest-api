package com.example.app.service;

import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.dto.article.DetailsArticleDTO;
import com.example.app.entities.Article;
import com.example.app.entities.Category;
import com.example.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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

    public <T> List<T> getAllArticle(Pageable page, Class<T> dtoType) {
        return articleRepository.findAll(page).getContent()
                .stream()
                .map(obj -> modelMapper.map(obj, dtoType))
                .collect(Collectors.toList());
    }

    public List<DetailsArticleDTO> getAllArticle(Pageable page) {
        return articleRepository.findAll(page).getContent()
                .stream()
                .map(article -> {
                    DetailsArticleDTO dto = modelMapper.map(article, DetailsArticleDTO.class);
                    dto.setAuthorId(article.getAppUser().getId());
                    dto.setCategoryIds(article.getCategories()
                            .stream().map(Category::getId).collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public long countArticle() {
        return articleRepository.count();
    }

    public void addCategoriesToArticle(Article article, List<String> categoryIds) {
        article.setCategories(new ArrayList<>());
        categoryIds.forEach(id -> {
            article.getCategories().add(categoryService.findById(id));
        });
    }

    public DetailsArticleDTO createNew(PostArticleRequest request, String username) {
        if (!userService.existByUsername(username)) {
            return null;
            // throw ?
        }

        Article article = modelMapper.map(request, Article.class);
        addCategoriesToArticle(article, request.getCategoryIds());
        article.setCreateDate(new Date(System.currentTimeMillis()));

        // article.setAppUser(AppUser.builder().id("e941be77-8f42-44ef-90e0-b5e22cde3123").build()); // lam nhu nay se mat het thong tin cua user id nay

        Article savedArticle = articleRepository.save(article);
        // articleRepository.setCreateByAppUserId(savedArticle.getId(), userService.getUserid(username));

        DetailsArticleDTO response = modelMapper.map(savedArticle, DetailsArticleDTO.class);
        response.setCategoryIds(request.getCategoryIds());
        response.setAuthorId(savedArticle.getAppUser().getId());
        return response;
    }
}
