package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaverecordDto{
    Integer id;
    EmployeeDto employeeID;
    Float quantity;
    Instant fromDate;
    Instant toDate;
    Boolean isAccept;
}