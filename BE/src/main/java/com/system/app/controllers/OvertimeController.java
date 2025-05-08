package com.system.app.controllers;

import com.system.app.dtos.OvertimeDto;
import com.system.app.exceptions.EntityAlreadyExistsException;
import com.system.app.models.OvertimeModel;
import com.system.app.services.OvertimeService;
import com.system.app.exceptions.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/overtimes")
public class OvertimeController {

    @Autowired
    private OvertimeService overtimeService;

    @GetMapping
    public ResponseEntity<Page<OvertimeDto>> getAllOvertimes(
            @RequestParam(required = false) LocalDate startDate,
            Pageable pageable) {
        Page<OvertimeDto> overtimes = overtimeService.getAllOvertime(startDate, pageable);
        return ResponseEntity.ok(overtimes);
    }

    @PostMapping
    public ResponseEntity<Object> createOvertime(@Valid @RequestBody OvertimeModel overtimeModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            OvertimeDto createdOvertime = overtimeService.createOvertime(overtimeModel);
            return ResponseEntity.ok(createdOvertime);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "Thời gian kết thúc phải sau thời gian bắt đầu và ngày làm thêm không được là ngày trong quá khứ."
            );
        } catch (EntityAlreadyExistsException e){
            return ResponseEntity.badRequest().body(
                    e.getMessage()
            );
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.\n" + e.getMessage()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOvertime(
            @PathVariable Integer id, @Valid @RequestBody OvertimeModel overtimeModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            OvertimeDto updatedOvertime = overtimeService.updateOvertime(id, overtimeModel);
            return ResponseEntity.ok(updatedOvertime);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Không tìm thấy tăng ca với ID: " + id
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "Thời gian kết thúc phải sau thời gian bắt đầu"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.\n" + e.getMessage()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOvertime(@PathVariable Integer id) {
        try {
            overtimeService.deleteOvertime(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Không tìm thấy tăng ca với ID: " + id
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "Không thể xóa tăng ca cũ hơn 1 tuần."
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau."
            );
        }
    }
}