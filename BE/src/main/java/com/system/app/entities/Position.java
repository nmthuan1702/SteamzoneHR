package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "positions")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PositionID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "PositionName", nullable = false, length = 100)
    private String positionName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DepartmentID", nullable = false)
    private Department departmentID;

}