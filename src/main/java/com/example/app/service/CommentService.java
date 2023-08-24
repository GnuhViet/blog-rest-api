package com.example.app.service;

import com.example.app.api.model.comment.CreateCommentRequest;
import com.example.app.dto.appuser.SimpleAppUserDTO;
import com.example.app.dto.comment.CommentDTO;
import com.example.app.entities.AppUser;
import com.example.app.entities.Article;
import com.example.app.entities.Comment;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommentDTO createNew(CreateCommentRequest request, String userId) {
        if (!articleService.existById(request.getArticleId())) {
            throw new NotFoundException("Article id not found");
        }
        if (!userService.existById(userId)) {
            throw new BadCredentialsException("Bad credential");
        }

        Article article = articleService.getReferenceById(request.getArticleId());
        AppUser user = userService.getReferenceById(userId);

        Comment savedComment = commentRepository.save(
                Comment.builder()
                        .appUser(user)
                        .article(article)
                        .content(request.getContent())
                        .createDate(new Date(System.currentTimeMillis()))
                        .build()
        );

        CommentDTO res = modelMapper.map(savedComment, CommentDTO.class);
        res.setUserDTO(userService.getUserDto(userId, SimpleAppUserDTO.class));

        return res;
    }

    public List<CommentDTO> getCommentsByArticleId(String id) {
        if (!articleService.existById(id)) {
            throw new NotFoundException("Article id not found");
        }

        List<Comment> comments = commentRepository.getCommentByArticle_Id(id);

        return comments.stream()
                .map(c -> {
                    CommentDTO dto = modelMapper.map(c, CommentDTO.class);
                    dto.setUserDTO(userService.getUserDto(c.getAppUser().getId(), SimpleAppUserDTO.class));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void delete(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment id not found"));

        if (!comment.getAppUser().getId().equals(userId)) {
            throw new BadCredentialsException("Bad credential");
        }

        commentRepository.delete(comment);
    }

    public CommentDTO updateComment(String commentId, CreateCommentRequest request, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment id not found"));

        if (!comment.getAppUser().getId().equals(userId)) {
            throw new BadCredentialsException("Bad credential");
        }

        comment.setContent(request.getContent());
        comment.setModifiedDate(new Date(System.currentTimeMillis()));

        CommentDTO res = modelMapper.map(comment, CommentDTO.class);
        res.setUserDTO(userService.getUserDto(userId, SimpleAppUserDTO.class));

        return res;
    }
}
