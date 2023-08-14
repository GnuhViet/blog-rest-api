package com.example.app.dto.article;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailsArticleDTO {
    private String id;
    private String title;
    private String thumbnail;
    private String shortDescription;
    private Date createDate;
    private List<String> categoryIds;
    private String authorId;
}
