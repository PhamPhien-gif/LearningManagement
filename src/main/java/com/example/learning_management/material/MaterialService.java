package com.example.learning_management.material;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.Course;
import com.example.learning_management.course.CourseRepository;
import com.example.learning_management.material.dto.AddMaterialRequest;
import com.example.learning_management.material.dto.DeleteMaterialResponse;
import com.example.learning_management.material.dto.MaterialSummary;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class MaterialService {
    final MaterialRepository materialRepository;
    final CourseRepository courseRepository;

    @Transactional
    public MaterialSummary addMaterial(AddMaterialRequest request, User instructor) {
        //check exists
        UUID courseId = request.getCourseId();
        boolean existsCourse = courseRepository.existsByIdAndInstructorId(courseId, instructor.getId());
        if (!existsCourse) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        
        Course course = courseRepository.getReferenceById(courseId);
        Material successMaterial = materialRepository.save(
                Material.builder()
                        .course(course)
                        .filePath(request.getFilePath())
                        .isPreview(request.getIsPreview())
                        .fileSize(request.getFileSize())
                        .fileType(request.getFilePath())
                        .title(request.getTitle())
                        .build());
        return MaterialSummary.from(successMaterial);
    }

    @Transactional
    public DeleteMaterialResponse deleteMaterial(UUID materialId, User instructor){
        UUID instructorId = instructor.getId();
        int success = materialRepository.deleteByIdAndInstructorId(materialId, instructorId);
        if(success==0){
            throw new AppException(ErrorCode.ENROLLMENT_DELETE_FAILED); 
        }
        return new DeleteMaterialResponse();
    }
}
