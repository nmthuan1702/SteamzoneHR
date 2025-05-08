package com.system.app.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeModel {

    @NotNull(message = "Ngày làm thêm không được để trống")
    private LocalDate overtimeDate;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private LocalTime endTime;

    @NotNull(message = "Mức lương theo giờ không được để trống")
    @Positive(message = "Mức lương theo giờ phải là số dương")
    private Integer hourlyRate;

    @NotNull(message = "Danh sách nhân viên không được để trống")
    @Size(min = 1, message = "Phải có ít nhất một nhân viên")
    private Set<Integer> employeeIds;

    public boolean isValidTimes() {
        return endTime.isAfter(startTime);
    }
}
