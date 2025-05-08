package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto{
    Integer id;
    String departmentName;
    Boolean isActive;
    Set<PositionDto> positions;
}