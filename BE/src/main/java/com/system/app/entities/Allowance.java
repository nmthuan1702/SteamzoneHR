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
@Table(name = "allowances")
public class Allowance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AllowanceID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "AllowanceName", nullable = false, length = 100)
    private String allowanceName;

    @NotNull
    @Column(name = "Amount", nullable = false)
    private Long amount;

    @OneToMany(mappedBy = "allowanceID", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Employeeallowance> employeeallowances = new LinkedHashSet<>();

}