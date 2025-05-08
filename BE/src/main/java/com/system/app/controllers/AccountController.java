package com.system.app.controllers;

import com.system.app.entities.Account;
import com.system.app.models.AccountModel;
import com.system.app.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // API tìm kiếm tài khoản với phân trang
    @GetMapping("/search")
    public ResponseEntity<Page<Account>> searchAccounts(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Account> accounts = accountService.searchAccounts(search, page, size);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // API lấy tài khoản theo id
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
        return accountService.getAccountById(id)
                .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // API tạo mới tài khoản
    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody @Valid AccountModel accountModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Account createdAccount = accountService.createAccount(accountModel);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi tạo tài khoản: " + e.getMessage());
        }
    }

    // API cập nhật tài khoản
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Integer id, @RequestBody @Valid AccountModel accountModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi xác thực chi tiết
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Account updatedAccount = accountService.updateAccount(id, accountModel);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi cập nhật tài khoản: " + e.getMessage());
        }
    }

    // API xóa tài khoản
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi xóa tài khoản: " + e.getMessage());
        }
    }
}
