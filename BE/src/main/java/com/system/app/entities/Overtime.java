package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "overtimes")
public class Overtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OvertimeID", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "OvertimeDate", nullable = false)
    private LocalDate overtimeDate;

    @NotNull
    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

    @NotNull
    @Column(name = "HourlyRate", nullable = false)
    private Integer hourlyRate;

    @OneToMany(mappedBy = "overtimeID", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Overtimerecord> overtimerecords = new LinkedHashSet<>();

}