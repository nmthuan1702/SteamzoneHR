package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "leaverecords")
public class Leaverecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveRecordID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private Float quantity;

    @NotNull
    @Column(name = "FromDate", nullable = false)
    private Instant fromDate;

    @NotNull
    @Column(name = "ToDate", nullable = false)
    private Instant toDate;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IsAccept", nullable = false)
    private Boolean isAccept = false;

}