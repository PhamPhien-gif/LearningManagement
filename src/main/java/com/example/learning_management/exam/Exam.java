package com.example.learning_management.exam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.example.learning_management.course.Course;
import com.example.learning_management.shared.BaseEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "exams")
public class Exam extends BaseEntity{
    private String name;
    private BigDecimal weight;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
    private String examUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY)
    private List<Submission> submissions;
}
