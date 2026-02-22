package com.example.learning_management.material.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMaterialRequest {
    private String title;
    private String filePath;
    private String fileType;
    private Integer fileSize;
    private Boolean isPreview;
}
