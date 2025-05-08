package com.system.app.controllers;

import com.system.app.dtos.EmployeeDto;
import com.system.app.models.EmployeeModel;
import com.system.app.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // API lấy danh sách nhân viên có phân trang, tìm kiếm và sắp xếp
    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> getEmployees(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<EmployeeDto> employees = employeeService.getAllEmployees(keyword, page, size);
        return ResponseEntity.ok(employees);
    }

    // API lấy thông tin nhân viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable Integer id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        if (employeeDto != null) {
            return ResponseEntity.ok(employeeDto);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Không tìm thấy nhân viên với ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // API thêm mới nhân viên
    @PostMapping
    public ResponseEntity<Object> createEmployee(@RequestBody @Valid EmployeeModel employeeModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        EmployeeDto createdEmployee = employeeService.createEmployee(employeeModel);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    // API cập nhật nhân viên
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable Integer id,
                                                 @RequestBody @Valid EmployeeModel employeeModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeModel);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<EmployeeDto> toggleStatus(@PathVariable Integer id) {
        EmployeeDto updatedEmployee = employeeService.toggleStatus(id);
        return ResponseEntity.ok(updatedEmployee);
    }

    // API xóa nhân viên
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Nhân viên đã được xóa thành công.");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getAlls")
    public ResponseEntity<?> getAlls(@RequestParam("years") Integer years,
                                     @RequestParam(value = "months", required = false) Integer months) {
        System.out.println(years);
        System.out.println(months);
        return ResponseEntity.ok(employeeService.getEmployee(years, months));
    }


    @GetMapping("/getSalary")
    public ResponseEntity<?> getsalary(@RequestParam("years") Integer years,
                                       @RequestParam(value = "months", required = false) Integer months) {
        return ResponseEntity.ok(employeeService.getSalary(years,months));
    }
}
