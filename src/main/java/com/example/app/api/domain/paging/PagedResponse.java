package com.example.app.api.domain.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> data;
    @JsonProperty("page_number")
    private int pageNumber;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("total_page")
    private int totalPage;
    @JsonProperty("total_records")
    private long totalRecords;
}
