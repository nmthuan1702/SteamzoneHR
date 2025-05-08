package com.system.app.controllers;

import com.system.app.dtos.AllowanceDto;
import com.system.app.models.AllowanceModel;
import com.system.app.services.AllowanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/allowances")
public class AllowanceController {

    @Autowired
    private AllowanceService allowanceService;

    // API lấy tất cả phụ cấp
    @GetMapping
    public ResponseEntity<Page<AllowanceDto>> getAllAllowances(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<AllowanceDto> allowances = allowanceService.getAllAllowances(keyword, pageable);
        return ResponseEntity.ok(allowances);
    }

    // API tạo mới phụ cấp
    @PostMapping
    public ResponseEntity<Object> createAllowance(@RequestBody @Valid AllowanceModel allowanceModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            AllowanceDto createdAllowance = allowanceService.createAllowance(allowanceModel);
            return new ResponseEntity<>(createdAllowance, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi tạo phụ cấp: " + e.getMessage());
        }
    }

    // API cập nhật phụ cấp
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAllowance(@PathVariable Integer id, @RequestBody @Valid AllowanceModel allowanceModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            AllowanceDto updatedAllowance = allowanceService.updateAllowance(id, allowanceModel);
            return new ResponseEntity<>(updatedAllowance, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi cập nhật phụ cấp: " + e.getMessage());
        }
    }

    // API xóa phụ cấp
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAllowance(@PathVariable Integer id) {
        try {
            allowanceService.deleteAllowance(id);

            // Tạo Map để trả về phản hồi JSON thành công
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thành công");
            response.put("details", "Đã xóa phụ cấp với ID: " + id);

            return ResponseEntity.noContent().build(); // hoặc nếu muốn trả lại JSON: return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Tạo Map để trả về phản hồi JSON lỗi
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Có lỗi xảy ra khi xóa phụ cấp");
            response.put("details", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
