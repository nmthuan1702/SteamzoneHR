package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "employeeallowances")
public class Employeeallowance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeAllowanceID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AllowanceID")
    private Allowance allowanceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID")
    private Employee employeeID;

    @NotNull
    @Column(name = "StartDate", nullable = false)
    private Instant startDate;

    @Column(name = "EndDate")
    private Instant endDate;

    @Column(name = "Amount")
    private Long amount;
}