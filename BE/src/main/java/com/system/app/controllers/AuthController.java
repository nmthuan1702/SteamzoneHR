package com.system.app.controllers;

import com.system.app.entities.Account;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.exceptions.MessagingException;
import com.system.app.models.LoginModel;
import com.system.app.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    // API đăng nhập
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginModel loginModel, BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();

        // Kiểm tra xem có lỗi xác thực không
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessages.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);  // Trả về lỗi nếu có
        }

        try {
            String token = authService.login(loginModel.getUsername(), loginModel.getPassword());
            response.put("token", token);
            response.put("message", "Đăng nhập thành công");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.put("token", null);
            response.put("message", e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }


    // API xác thực token
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        try {
            Account account = authService.validateToken(token);
            return ResponseEntity.ok("Token hợp lệ cho tài khoản: " + account.getUsername());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            authService.forgotPassword(email);
            return ResponseEntity.ok("Email reset mật khẩu đã được gửi.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Lỗi khi gửi email reset mật khẩu.");
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token) {
        String title = "Đặt lại mật khẩu thất bại!";
        String message = "Có lỗi xảy ra.";
        String content = "Vui lòng thử lại sau vài phút.";

        try {
            // Thực hiện reset mật khẩu
            authService.resetPassword(token);

            // Cập nhật thông tin thành công
            title = "Đặt lại mật khẩu thành công!";
            message = "Cảm ơn bạn!";
            content = "Mật khẩu tạm thời đã được gửi qua email. Vui lòng kiểm tra hộp thư của bạn.";

            // Trả về thông tin thành công với HTML
            return ResponseEntity.ok(generateHtml(title, message, content));
        } catch (EntityNotFoundException e) {
            // Trả về thông tin lỗi khi không tìm thấy tài khoản
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateHtml(title, "Không tìm thấy tài khoản phù hợp.", content));
        } catch (MessagingException e) {
            // Trả về thông tin lỗi khi gửi email không thành công
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generateHtml("Lỗi khi gửi email!", "Có lỗi xảy ra khi gửi mật khẩu tạm thời qua email.", content));
        }
    }

    private String generateHtml(String title, String message,String content) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>" + title + "</title>" +
                "<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">" +
                "</head>" +
                "<body>" +
                "<div class=\"grid h-screen place-content-center bg-white px-4\">" +
                "<div class=\"text-center\">" +
                "<h1 class=\"text-9xl font-black text-gray-200\">" + title + "</h1>" +
                "<p class=\"text-2xl font-bold tracking-tight text-gray-900 sm:text-4xl\">" + message + "</p>" +
                "<p class=\"mt-4 text-gray-500\">" + content + "</p>" +
                "<a href=\"http://localhost:3000/login\" class=\"mt-6 inline-block rounded bg-indigo-600 px-5 py-3 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring\">" +
                "Đi đến trang đăng nhập" +
                "</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

}
