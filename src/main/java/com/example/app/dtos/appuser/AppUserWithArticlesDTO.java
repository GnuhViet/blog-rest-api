package com.example.app.dtos.appuser;

import com.example.app.dtos.article.SimpleArticleDTO;
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
