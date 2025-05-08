package com.system.app.dtos;

import com.system.app.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvertimerecordDto{
    Integer id;
    Integer employeeID;
}