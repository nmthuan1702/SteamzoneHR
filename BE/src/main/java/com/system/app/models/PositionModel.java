package com.system.app.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionModel {

    @NotNull(message = "Mã phòng ban không được để trống")
    private Integer departmentId;

    @NotBlank(message = "Tên chức vụ không được để trống")
    private String positionName;
}
