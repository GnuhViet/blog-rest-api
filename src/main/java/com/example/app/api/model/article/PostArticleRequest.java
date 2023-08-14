package com.example.app.api.model.article;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostArticleRequest {
    private String id;
    private String title;
    private String thumbnail;
    private String shortDescription;
    private String content;
    private List<String> categoryIds; //TODO
}
