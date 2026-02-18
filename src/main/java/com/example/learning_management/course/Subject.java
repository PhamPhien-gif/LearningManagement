package com.example.learning_management.course;

import java.util.List;
import com.example.learning_management.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subjects")
public class Subject extends BaseEntity{

    private String name;
    private String code;
    
    //Recursive relationship
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "subject_prerequisites",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_subject_id")
    )
    //List subjects have to finish to study
    private List<Subject> prerequisites;

    //List subjects unlock after finish this subject
    @ManyToMany(mappedBy = "prerequisites")
    private List<Subject> requireFor;

}
