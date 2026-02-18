package com.example.learning_management.shared;

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
}
