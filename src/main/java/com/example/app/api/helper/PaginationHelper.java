package com.example.app.api.helper;

import com.example.app.api.domain.paging.PagedResponse;
import com.example.app.api.domain.paging.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PaginationHelper {

    public static <T> PagedResponse<T> createPagedResponse(PaginationRequest request, List<T> pagedData, long totalRecord, String route) {
        final int pageNumber = request.getPageNumber();
        final int pageSize = request.getPageSize();
        final int totalPages = Double.valueOf(Math.ceil((double) totalRecord / pageSize)).intValue();

        return PagedResponse.<T>builder()
                .data(pagedData)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPage(totalPages)
                .totalRecords(totalRecord)
                .build();
    }

    public static Pageable parsePagingRequest(PaginationRequest request) { // TODO refactor
        if (request.getShortBy() == null) {
            return PageRequest.of(request.getPageNumber(), request.getPageSize());
        }

        Sort sort = request.getShortDir().equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(request.getShortBy()).ascending() : Sort.by(request.getShortBy()).descending();

        return PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
    }
}
