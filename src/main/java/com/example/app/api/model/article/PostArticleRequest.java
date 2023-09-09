package com.example.app.api.model.article;

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
    private String title;
    private String thumbnail;
    private String shortDescription;
    private String content;
    private List<String> categoryIds; //TODO
}
