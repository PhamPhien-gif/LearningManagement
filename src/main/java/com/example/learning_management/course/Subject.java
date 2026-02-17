package com.example.learning_management.course;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String code;
    
    //Recursive relationship
    @ManyToMany
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
