package com.example.learning_management.material.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.learning_management.material.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MaterialDetail {
    private UUID id;
    private String title;
    private String filePath;
    private String fileType;
    private Integer fileSize;
    private Boolean isPreview;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MaterialDetail from(Material material){
        return MaterialDetail.builder()
                        .id(material.getId())
                        .title(material.getTitle())
                        .filePath(material.getFilePath())
                        .fileType(material.getFileType())
                        .fileSize(material.getFileSize())
                        .isPreview(material.getIsPreview())
                        .createdAt(material.getCreatedAt())
                        .updatedAt(material.getUpdatedAt())
                        .build();
    }
}
