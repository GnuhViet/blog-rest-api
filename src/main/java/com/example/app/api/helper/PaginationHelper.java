package com.example.app.api.helper;

import com.example.app.api.domain.PagedResponse;
import com.example.app.api.domain.PaginationRequest;
import com.example.app.api.domain.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class PaginationHelper {
    private static final String baseUri = "http://localhost:8080/";

    public static String getPageUri(PaginationRequest request, String route) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUri + route)
                .queryParam("pageNumber", request.getPageNumber())
                .queryParam("pageSize", request.getPageSize());
        return builder.build().toUri().toString();
    }

    public static <T> PagedResponse<T> createPagedResponse(PaginationRequest request, List<T> pagedData, int totalRecord, String route) {
        PagedResponse<T> response = new PagedResponse<T>(pagedData, request.getPageNumber(), request.getPageSize());
        int totalPages = Double.valueOf(Math.ceil((double) totalRecord / request.getPageSize())).intValue();

        response.setNextPage(
                response.getPageNumber() >= 0 && response.getPageNumber() < totalPages
                        ? getPageUri(PaginationRequest.builder()
                                .pageNumber(request.getPageNumber() + 1)
                                .pageSize(request.getPageSize())
                                .build(),
                        route)
                        : null
        );

        response.setPreviousPage(
                response.getPageNumber() >= 1 && response.getPageNumber() < totalPages
                        ? getPageUri(PaginationRequest.builder()
                                .pageNumber(request.getPageNumber() - 1)
                                .pageSize(request.getPageSize())
                                .build(),
                        route)
                        : null
        );

        response.setFirstPage(getPageUri(PaginationRequest.builder()
                        .pageNumber(0)
                        .pageSize(request.getPageSize())
                        .build(),
                route)
        );

        response.setLastPage(getPageUri(PaginationRequest.builder()
                        .pageNumber(totalPages)
                        .pageSize(request.getPageSize())
                        .build(),
                route)
        );

        response.setTotalPage(totalPages);
        response.setTotalRecords(totalRecord);

        return response;
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
