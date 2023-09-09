package com.example.app.api.model.article;

import com.example.app.dtos.appuser.SimpleAppUserDTO;
import com.example.app.dtos.category.CategoryDTO;
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
public class DetailsArticleResponse {
    private String id;
    private String title;
    private String thumbnail;
    private String shortDescription;
    private Date createDate;
    private Date modifiedDate;
    private List<CategoryDTO> categoryDTOList;
    private SimpleAppUserDTO author;
}
