package com.example.app.service;

import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.dto.article.DetailsArticleDTO;
import com.example.app.entities.AppUser;
import com.example.app.entities.Article;
import com.example.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public <T> List<T> getAllArticle(Pageable page, Class<T> dtoType) {
        List<Article> res = articleRepository.findAll(page).getContent();
        return articleRepository.findAll(page).getContent()
                .stream()
                .map(obj -> modelMapper.map(obj, dtoType))
                .collect(Collectors.toList());
    }

    public long countArticle() {
        return articleRepository.count();
    }


    public DetailsArticleDTO createNew(PostArticleRequest request, String username) {
        if (!userService.existByUsername(username)) {
            return null;

            // throw ?
        }

        articleRepository.save(modelMapper.map(request, Article.class));
        return null;
    }
}
