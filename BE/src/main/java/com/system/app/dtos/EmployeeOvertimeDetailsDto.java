package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOvertimeDetailsDto {
    String fullName;
    String DepartmentName;
    String PositionName;
    Double Quantity;
    Long totalOvertimeHours;
}
