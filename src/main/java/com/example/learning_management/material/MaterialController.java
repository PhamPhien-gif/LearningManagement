package com.example.learning_management.material;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.learning_management.material.dto.AddMaterialRequest;
import com.example.learning_management.material.dto.DeleteMaterialResponse;
import com.example.learning_management.material.dto.MaterialDetail;
import com.example.learning_management.material.dto.UpdateMaterialRequest;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/material")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/upload")
    public ResponseEntity<MaterialDetail> addMaterial(
        @RequestBody AddMaterialRequest request,
        @AuthenticationPrincipal User instructor
    ){
        return ResponseEntity.ok(materialService.addMaterial(request, instructor));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{materialId}")
    public ResponseEntity<DeleteMaterialResponse> deleteMaterial(
        @PathVariable UUID materialId,
        @AuthenticationPrincipal User instructor
    ){
        return ResponseEntity.ok(materialService.deleteMaterial(materialId, instructor));
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialDetail> getMaterial(
        @PathVariable UUID materialId,
        @AuthenticationPrincipal User viewer
    ){
        return ResponseEntity.ok(materialService.getMaterial(materialId, viewer));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PatchMapping("/{materialId}")
    public ResponseEntity<MaterialDetail> updateMaterial(
        @PathVariable UUID materialId,
        @RequestBody UpdateMaterialRequest request,
        @AuthenticationPrincipal User instructor
    ){
        return ResponseEntity.ok(materialService.updateMaterial(materialId, request, instructor));
    }
}
