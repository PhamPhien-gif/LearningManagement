package com.example.learning_management.shared;

import org.springframework.data.domain.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageResponse {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private boolean isLast;
    private int totalElements;

    public PageResponse(Page<?> page) {
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.isLast = page.isLast();
        this.totalElements = page.getNumberOfElements();
    }
}
