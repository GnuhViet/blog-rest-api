package com.example.app.repository;

import com.example.app.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query(
            value = "select * from article a where lower(a.title) like :title%",
            countQuery = "select count(*) from article",
            nativeQuery = true
    )
    Page<Article> findArticleByTitle(@Param("title") String title, Pageable pageable);


}
