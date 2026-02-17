package com.example.learning_management.course;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.learning_management.shared.BaseEntity;
import com.example.learning_management.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.FetchType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course extends BaseEntity {

    @NotNull(message = "Subject must not be null")
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @NotNull(message = "Instructor must not be null")
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrar_id")
    private User registrar;

    @Min(value = 1, message = "INVALID_COURSE_CAPACITY")
    @Max(value = 120, message = "INVALID_COURSE_CAPACITY" )
    private Integer maxStudents;

    @NotNull(message = "Time begin must not be null")
    private LocalDateTime timeBegin;

    @NotNull(message = "Time end must not be null")
    private LocalDateTime timeEnd;

}
