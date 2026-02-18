package com.example.learning_management.material.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialSummary {
    private UUID id;
    private String title;
    private String fileType;
    private int fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isPreview;
}
