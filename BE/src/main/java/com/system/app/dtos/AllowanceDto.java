package com.system.app.dtos;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllowanceDto {
    Integer id;
    String allowanceName;
    Long amount;
    Set<EmployeeallowanceDto> employeeallowances;
}