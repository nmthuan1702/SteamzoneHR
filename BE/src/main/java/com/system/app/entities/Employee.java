package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PositionID", nullable = false)
    private Position positionID;

    @Size(max = 100)
    @NotNull
    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @NotNull
    @Column(name = "Gender", nullable = false)
    private Boolean gender = false;

    @NotNull
    @Column(name = "BirthDate", nullable = false)
    private LocalDate birthDate;

    @Size(max = 255)
    @NotNull
    @Column(name = "Email", nullable = false)
    private String email;

    @Size(max = 12)
    @NotNull
    @Column(name = "PhoneNumber", nullable = false, length = 12)
    private String phoneNumber;

    @Size(max = 12)
    @NotNull
    @Column(name = "IDCardNumber", nullable = false, length = 12)
    private String iDCardNumber;

    @Size(max = 255)
    @Column(name = "Address")
    private String address;

    @Column(name = "AvatarURL")
    private String avatarURL;

    @NotNull
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = false;

    @OneToMany(mappedBy = "employeeID")
    private Set<Contract> contracts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "employeeID")
    private Set<Employeeallowance> employeeallowances = new LinkedHashSet<>();

    @OneToMany(mappedBy = "employeeID")
    private Set<Overtimerecord> overtimerecords = new LinkedHashSet<>();

}