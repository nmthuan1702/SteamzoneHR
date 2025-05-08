package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContractID", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "StartDate", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "EndDate", nullable = false)
    private Instant endDate;

    @NotNull
    @Column(name = "SigningDate", nullable = false)
    private Instant signingDate;

    @NotNull
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = false;

    @NotNull
    @Column(name = "Salary", nullable = false)
    private Long salary;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

}