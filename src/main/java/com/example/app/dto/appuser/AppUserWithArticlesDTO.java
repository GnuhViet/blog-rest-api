package com.example.app.dto.appuser;

import com.example.app.dto.article.SimpleArticleDTO;
import com.example.app.entities.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserWithArticlesDTO {
    private String username;
    private List<SimpleArticleDTO> articles;
}
