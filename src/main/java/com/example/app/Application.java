package com.example.app;

import com.example.app.api.model.article.PostArticleRequest;
import com.example.app.api.model.comment.CreateCommentRequest;
import com.example.app.dtos.appuser.DetailsAppUserDTO;
import com.example.app.dtos.article.DetailsArticleDTO;
import com.example.app.dtos.category.CategoryDTO;
import com.example.app.entities.AppUser;
import com.example.app.entities.Role;
import com.example.app.service.ArticleService;
import com.example.app.service.CategoryService;
import com.example.app.service.CommentService;
import com.example.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class Application {
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService,
                          CategoryService categoryService,
                          ArticleService articleService,
                          CommentService commentService
    ) {
        return args -> {
            List<DetailsAppUserDTO> user = new ArrayList<>();
            List<CategoryDTO> category = new ArrayList<>();
            List<DetailsArticleDTO> articles = new ArrayList<>();

            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));

            user.add(userService.saveUser(AppUser.builder()
                    .username("string")
                    .password(passwordEncoder.encode("string"))
                    .build()
            ));

            userService.addRoleToUser("string", "ROLE_USER");
            userService.addRoleToUser("string", "ROLE_ADMIN");

            user.add(userService.saveUser(AppUser.builder()
                    .username("user")
                    .password(passwordEncoder.encode("string"))
                    .build()
            ));

            userService.addRoleToUser("user", "ROLE_USER");

            category.add(categoryService.save("Coding"));
            category.add(categoryService.save("Manga"));
            category.add(categoryService.save("Facts"));


            articles.add(articleService.createNew(
                    PostArticleRequest.builder()
                            .title("Bai viet 1")
                            .content("abcd")
                            .shortDescription("descript")
                            .categoryIds(List.of(category.get(0).getId()))
                            .build(),
                    user.get(0).getId()
            ));

            articles.add(articleService.createNew(
                    PostArticleRequest.builder()
                            .title("Bai viet 2")
                            .content("abcd")
                            .shortDescription("descript")
                            .categoryIds(category.stream().map(CategoryDTO::getId).collect(Collectors.toList()))
                            .build(),
                    user.get(1).getId()
            ));

            articles.add(articleService.createNew(
                    PostArticleRequest.builder()
                            .title("Hello world")
                            .content("abcd")
                            .shortDescription("descript")
                            .categoryIds(category.stream().map(CategoryDTO::getId).collect(Collectors.toList()))
                            .build(),
                    user.get(1).getId()
            ));

            commentService.createNew(
                    CreateCommentRequest.builder()
                            .articleId(articles.get(0).getId())
                            .content("Comment 1")
                            .build(),
                    user.get(0).getId()
            );
            commentService.createNew(
                    CreateCommentRequest.builder()
                            .articleId(articles.get(0).getId())
                            .content("Comment 2")
                            .build(),
                    user.get(0).getId()
            );
            commentService.createNew(
                    CreateCommentRequest.builder()
                            .articleId(articles.get(0).getId())
                            .content("Comment 3")
                            .build(),
                    user.get(1).getId()
            );

            log.info("Finish test data initial");
        };
    }

}
