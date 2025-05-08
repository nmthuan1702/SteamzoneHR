package com.system.app.controllers;

import com.system.app.dtos.PositionDto;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.PositionModel;
import com.system.app.services.PositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    // Lấy danh sách chức vụ, có thể tìm kiếm theo tên
    @GetMapping
    public ResponseEntity<List<PositionDto>> getPositions(
            @RequestParam(value = "positionName", defaultValue = "") String positionName) {

        List<PositionDto> positions = positionService.searchPositions(positionName);
        if (positions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(positions);
    }

    // Lấy thông tin chức vụ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPositionById(@PathVariable Integer id) {
        try {
            PositionDto position = positionService.getPositionById(id);
            return ResponseEntity.ok(position);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy chức vụ với id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra trong quá trình xử lý yêu cầu: " + e.getMessage());
        }
    }

    // API tạo mới chức vụ
    @PostMapping
    public ResponseEntity<Object> createPosition(@RequestBody @Valid PositionModel positionModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            PositionDto createdPosition = positionService.createPosition(positionModel);
            return new ResponseEntity<>(createdPosition, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi tạo mới chức vụ: " + e.getMessage());
        }
    }

    // API cập nhật chức vụ
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePosition(@PathVariable Integer id, @RequestBody @Valid PositionModel positionModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            PositionDto updatedPosition = positionService.updatePosition(id, positionModel);
            return ResponseEntity.ok(updatedPosition);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy chức vụ với id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi cập nhật chức vụ: " + e.getMessage());
        }
    }

    // API xóa chức vụ
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePosition(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            positionService.deletePosition(id);
            response.put("status", "success");
            response.put("message", "Chức vụ đã được xóa thành công.");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            response.put("status", "error");
            response.put("message", "Không thể xóa chức vụ này vì có nhân viên đang sử dụng nó.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (EntityNotFoundException e) {
            response.put("status", "error");
            response.put("message", "Không tìm thấy chức vụ với id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Có lỗi xảy ra trong quá trình xóa chức vụ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
