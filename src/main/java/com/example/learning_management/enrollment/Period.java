package com.example.learning_management.enrollment;

import java.time.LocalDateTime;
import java.util.List;

import com.example.learning_management.course.Course;
import com.example.learning_management.shared.BaseEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "enrollment_periods")
public class Period extends BaseEntity{
    private String name;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;

    @OneToMany(mappedBy = "period", fetch = FetchType.LAZY)
    private List<Course> courses;
}
