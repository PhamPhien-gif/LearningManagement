package com.example.learning_management.material.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMaterialRequest {
    private UUID courseId;
    private String title;
    private String fileType;
    private int fileSize;
    private Boolean isPreview;
    private String filePath;
}
