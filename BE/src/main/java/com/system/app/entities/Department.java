package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DepartmentID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

    @NotNull
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = false;

    @OneToMany(mappedBy = "departmentID")
    private Set<Position> positions = new LinkedHashSet<>();

}