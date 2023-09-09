package com.example.app.repository;

import com.example.app.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> getCommentByArticle_Id(String articleId);
}
