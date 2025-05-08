package com.system.app.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeModel {

    @NotNull(message = "Vị trí công việc không được để trống")
    private Integer positionID;

    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Size(min = 3, max = 100, message = "Tên đầy đủ phải có độ dài từ 3 đến 100 ký tự")
    private String fullName;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean gender;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDate;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Địa chỉ email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "Số CMND/CCCD không được để trống")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Số CMND/CCCD không hợp lệ")
    private String iDCardNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private String avatarURL;
}
