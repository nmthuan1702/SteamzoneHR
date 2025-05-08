package com.system.app.controllers;

import com.system.app.dtos.DepartmentDto;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.DepartmentModel;
import com.system.app.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getDepartments(
            @RequestParam(value = "departmentName", defaultValue = "") String departmentName,
            @RequestParam(value = "sort", defaultValue = "departmentName") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());

        List<DepartmentDto> departments = departmentService.getDepartments(departmentName, sortBy, sortDirection);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DepartmentDto>> getActiveDepartments() {
        List<DepartmentDto> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(departments);
    }

    // API lấy phòng ban theo ID
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Integer id) {
        DepartmentDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    // API tạo mới phòng ban
    @PostMapping
    public ResponseEntity<Object> createDepartment(@RequestBody @Valid DepartmentModel departmentModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        DepartmentDto createdDepartment = departmentService.createDepartment(departmentModel);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    // API cập nhật phòng ban
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDepartment(@PathVariable Integer id, @RequestBody @Valid DepartmentModel departmentModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentModel);
        return ResponseEntity.ok(updatedDepartment);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Object> toggleDepartmentStatus(@PathVariable Integer id) {
        try {
            DepartmentDto updatedDepartment = departmentService.toggleDepartmentStatus(id);
            return ResponseEntity.ok(updatedDepartment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy phòng ban với id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra trong quá trình thay đổi trạng thái phòng ban: " + e.getMessage());
        }
    }

    // API xóa phòng ban
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            departmentService.deleteDepartment(id);
            response.put("status", "success");
            response.put("message", "Phòng ban đã được xóa thành công.");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            response.put("status", "error");
            response.put("message", "Không thể xóa phòng ban này vì có chức vụ đang tham chiếu đến nó.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (EntityNotFoundException e) {
            response.put("status", "error");
            response.put("message", "Không tìm thấy phòng ban với id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Có lỗi xảy ra trong quá trình xóa phòng ban: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
