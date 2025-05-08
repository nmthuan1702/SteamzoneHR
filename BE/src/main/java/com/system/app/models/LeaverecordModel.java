package com.system.app.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaverecordModel {

    @NotNull(message = "ID nhân viên không được để trống")
    private Integer employeeID;

    @NotNull(message = "Số ngày nghỉ phép không được để trống")
    @Positive(message = "Số ngày nghỉ phép phải là số dương")
    private Float quantity;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private Instant fromDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private Instant toDate;

    @NotNull(message = "Không được để trống loại nghỉ phép")
    private Boolean isAccept;
}
