package com.example.app.api.model.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> data;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private long totalRecords;
}
