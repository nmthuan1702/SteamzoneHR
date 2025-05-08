package com.system.app.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractModel {

    @NotNull(message = "Ngày bắt đầu hợp đồng không được để trống")
    private Instant startDate;

    @NotNull(message = "Ngày kết thúc hợp đồng không được để trống")
    private Instant endDate;

    @NotNull(message = "Ngày ký hợp đồng không được để trống")
    private Instant signingDate;

    @NotNull(message = "Mức lương không được để trống")
    private Long salary;

    @NotNull(message = "ID nhân viên không được để trống")
    private Integer employeeID;
}
