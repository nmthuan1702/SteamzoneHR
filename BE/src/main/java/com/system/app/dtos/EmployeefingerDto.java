package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeefingerDto{
    Integer id;
    String fingerCode;
    EmployeeDto employeeInfoID;
}