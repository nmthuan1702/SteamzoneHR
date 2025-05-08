package com.system.app.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllowanceModel {

    @NotBlank(message = "Tên phụ cấp không được để trống")
    @Size(min = 3, max = 100, message = "Tên phụ cấp phải có độ dài từ 3 đến 100 ký tự")
    private String allowanceName;

    @NotNull(message = "Số tiền không được để trống")
    @PositiveOrZero(message = "Số tiền phải là giá trị dương hoặc bằng 0")
    private Long amount;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private Instant startDate;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private Instant endDate;

    @NotNull(message = "Danh sách nhân viên không được để trống")
    private Set<Integer> employeeIds;
}
