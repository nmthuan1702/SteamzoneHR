package com.system.app.controllers;

import com.system.app.dtos.SalaryDTO;
import com.system.app.services.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    // Lấy lương của tất cả nhân viên trong một năm (và có thể là tháng nếu có)
    @GetMapping
    public ResponseEntity<List<SalaryDTO>> getAllSalaries(
            @RequestParam("year") Integer year,
            @RequestParam(value = "month", required = false) Integer month) {

        List<SalaryDTO> salaries = salaryService.getAllSalaries(year, month);
        if (salaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salaries);
    }
}
