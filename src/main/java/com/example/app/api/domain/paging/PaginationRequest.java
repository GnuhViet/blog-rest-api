package com.example.app.api.domain.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationRequest {
    @JsonProperty("page_number")
    @Min(0)
    private int pageNumber;
    @JsonProperty("page_size")
    @Min(1) @Max(10)
    private int pageSize;
    @JsonProperty("short_by")
    private String shortBy;
    @JsonProperty("short_dir")
    private String shortDir;
}
