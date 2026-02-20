package com.example.learning_management.enrollment.dto;

import java.util.List;
import com.example.learning_management.shared.PageResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
public class AllEnrollmentResponse extends PageResponse{
    List<EnrollResponse> enrolls;
}
