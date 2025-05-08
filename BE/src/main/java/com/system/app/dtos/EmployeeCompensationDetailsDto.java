package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCompensationDetailsDto {
    String fullName;
    String DepartmentName;
    String PositionName;
    Long salary;          // lương cơ bản
    Long totalAllowance;  // tổng phụ cấp
    Long totalOvertimeHours; // tổng số giờ làm
    Long totalSalary;
}
