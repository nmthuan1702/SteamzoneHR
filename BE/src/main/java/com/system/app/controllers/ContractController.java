package com.system.app.controllers;

import com.system.app.dtos.ContractDto;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.ContractModel;
import com.system.app.services.ContractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping
    public ResponseEntity<Page<ContractDto>> getAllContracts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<ContractDto> contracts = contractService.getAllContracts(keyword, pageable);
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getContractById(@PathVariable Integer id) {
        try {
            ContractDto contract = contractService.getContractById(id);
            return ResponseEntity.ok(contract);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hợp đồng với ID: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createContract(@RequestBody @Valid ContractModel contractModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        ContractDto newContract = contractService.createContract(contractModel);
        return new ResponseEntity<>(newContract, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateContract(@PathVariable Integer id, @RequestBody @Valid ContractModel contractModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            ContractDto updatedContract = contractService.updateContract(id, contractModel);
            return ResponseEntity.ok(updatedContract);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hợp đồng với ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteContract(@PathVariable Integer id) {
        try {
            contractService.deleteContract(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hợp đồng với ID: " + id);
        }
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Object> toggleContractStatus(@PathVariable Integer id) {
        try {
            ContractDto updatedContract = contractService.toggleContractStatus(id);
            return ResponseEntity.ok(updatedContract);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hợp đồng với ID: " + id);
        }
    }
}
