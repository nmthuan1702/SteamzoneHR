package com.system.app.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeefingerModel {

    @NotBlank(message = "Mã vân tay không được để trống")
    @Size(min = 6, max = 50, message = "Mã vân tay phải có độ dài từ 6 đến 50 ký tự")
    private String fingerCode;

    @NotNull(message = "ID nhân viên không được để trống")
    private Integer employeeID;
}