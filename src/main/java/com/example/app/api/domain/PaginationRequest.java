package com.example.app.api.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationRequest {
    @Min(0)
    private int pageNumber;
    @Min(1) @Max(1)
    private int pageSize;
    private String shortBy;
    private String shortDir;
}
