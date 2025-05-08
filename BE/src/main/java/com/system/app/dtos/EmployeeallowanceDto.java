package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeallowanceDto{
    Integer id;
    String allowanceName;
    EmployeeDto employeeID;
    Instant startDate;
    Instant endDate;
    Long amount;
}