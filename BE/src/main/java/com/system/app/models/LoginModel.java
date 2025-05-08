package com.system.app.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginModel {
    @NotBlank(message = "Không được bỏ trống tên đăng nhập")
    private String username;

    @NotBlank(message = "Không được bỏ trống tên mật khẩu")
    private String password;
}
