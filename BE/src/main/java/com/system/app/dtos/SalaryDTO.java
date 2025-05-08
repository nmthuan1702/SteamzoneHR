package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SalaryDTO {
    String employeeName;
    String avatar;
    String position;
    String department;
    Long workDays;
    double leaveDays;
    Long allowance;
    Long overtimeHours;
    Long overtimeMoneys;
    Long baseSalary;
    Long totalBaseSalary;
    Long totalSalary;
}
