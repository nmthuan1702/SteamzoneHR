package com.system.app.controllers;

import com.system.app.dtos.LeaverecordDto;
import com.system.app.models.LeaverecordModel;
import com.system.app.services.LeaverecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/leaverecords")
public class LeaverecordController {

    @Autowired
    private LeaverecordService leaverecordService;

    @GetMapping
    public ResponseEntity<Page<LeaverecordDto>> getAllLeaverecords(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
            Pageable pageable) {
        Page<LeaverecordDto> leaverecords = leaverecordService.getAllLeaverecords(keyword, fromDate, toDate, pageable);
        return ResponseEntity.ok(leaverecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaverecordDto> getLeaverecordById(@PathVariable Integer id) {
        LeaverecordDto leaverecord = leaverecordService.getLeaverecordById(id);
        return ResponseEntity.ok(leaverecord);
    }

    @PostMapping
    public ResponseEntity<Object> createLeaverecord(@RequestBody @Valid LeaverecordModel leaverecordModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        LeaverecordDto createdRecord = leaverecordService.createLeaverecord(leaverecordModel);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaverecord(@PathVariable Integer id) {
        leaverecordService.deleteLeaverecord(id);
        return ResponseEntity.noContent().build();
    }
}
