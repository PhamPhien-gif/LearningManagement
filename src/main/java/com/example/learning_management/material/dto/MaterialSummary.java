package com.example.learning_management.material.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.learning_management.material.Material;
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
    public static MaterialSummary from(Material material){
        return MaterialSummary.builder()
                            .id(material.getId())
                            .title(material.getTitle())
                            .fileType(material.getFileType())
                            .fileSize(material.getFileSize())
                            .createdAt(material.getCreatedAt())
                            .updatedAt(material.getUpdatedAt())
                            .isPreview(material.getIsPreview())
                            .build();
    }
}
