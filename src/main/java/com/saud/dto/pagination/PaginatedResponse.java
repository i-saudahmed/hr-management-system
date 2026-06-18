package com.saud.dto.pagination;

import lombok.*;

import java.util.List;


@Getter
@Setter
public class PaginatedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
    private boolean first;
    private boolean last;

    public PaginatedResponse() {
    }

    public PaginatedResponse(List<T> content, int totalPages, long totalElements, int pageNumber, int pageSize, boolean first, boolean last) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.first = first;
        this.last = last;
    }
}
