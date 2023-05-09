package com.example.app.api.domain;

import lombok.*;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class PagedResponse<T> extends Response<T> {
    private int pageNumber;
    private int pageSize;
    private String firstPage;
    private String lastPage;
    private int totalPage;
    private int totalRecords;
    private String nextPage;
    private String previousPage;

    public PagedResponse (List<T> data, int pageNumber, int pageSize) {
        this.setData(data);
    }
}
