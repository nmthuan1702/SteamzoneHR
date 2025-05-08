package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeDto{
    Integer id;
    LocalDate overtimeDate;
    LocalTime startTime;
    LocalTime endTime;
    Integer hourlyRate;
    Set<OvertimerecordDto> overtimerecords;
}