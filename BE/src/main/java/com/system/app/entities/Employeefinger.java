package com.system.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employeefingers")
public class Employeefinger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FingerID", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "FingerCode")
    private String fingerCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID")
    private Employee employeeID;

}