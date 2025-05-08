package com.system.app.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentModel {
    @NotBlank(message = "Tên phòng ban không được để trống")
    @Size(min = 3, max = 100, message = "Tên phòng ban phải có độ dài từ 3 đến 100 ký tự")
    private String departmentName;
}
