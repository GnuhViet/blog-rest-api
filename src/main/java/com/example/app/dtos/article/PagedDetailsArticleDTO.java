package com.example.app.dtos.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedDetailsArticleDTO {
    private List<DetailsArticleDTO> detailsArticleDTOs;
    private long totalRecords;
}
