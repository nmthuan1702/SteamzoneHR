package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatisticsDTO {
    String fullName;
    String departmentName;
    String positionName;
    Integer overtimeHours;    //giờ làm thêm
    Integer totalOnTimeDays;  //tổng ngày công
    Integer totalLateDays;    // số ngày đi trễ
    Integer daysAbsent;       // số ngày nghỉ
}
