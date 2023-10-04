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
            countQuery = "select count(*) from article a where lower(a.title) like :title%",
            nativeQuery = true
    )
    Page<Article> findArticleByTitle(@Param("title") String title, Pageable pageable);

    @Query(
            value = "select * from article a " +
                    "join article_category a_c " +
                    "where a.id = a_c.article_id and :categoryId = a_c.category_id",
            countQuery = "select count(*) from article a " +
                    "join article_category a_c " +
                    "where a.id = a_c.article_id and :categoryId = a_c.category_id",
            nativeQuery = true
    )
    Page<Article> findArticleByCategory(@Param("categoryId") String categoryId, Pageable pageable);

    @Query(
            value = "select * from article a " +
                    "join article_category a_c " +
                    "where a.id = a_c.article_id and :categoryId = a_c.category_id and lower(a.title) like :title%",
            countQuery = "select count(*) from article a " +
                    "join article_category a_c " +
                    "where a.id = a_c.article_id and :categoryId = a_c.category_id and lower(a.title) like :title%",
            nativeQuery = true
    )
    Page<Article> findArticleByTitleAndCategory(@Param("title") String title, @Param("categoryId") String categoryId, Pageable pageable);
}
