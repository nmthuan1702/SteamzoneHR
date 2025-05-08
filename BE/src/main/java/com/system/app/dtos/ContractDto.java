package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto{
    Integer id;
    Instant startDate;
    Instant endDate;
    Instant signingDate;
    Boolean isActive;
    Long salary;
    EmployeeDto employeeID;
}