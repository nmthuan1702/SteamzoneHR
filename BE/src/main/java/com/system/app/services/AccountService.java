package com.system.app.services;

import com.system.app.dtos.AccountDto;
import com.system.app.entities.Account;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.AccountModel;
import com.system.app.repositories.AccountRepository;
import com.system.app.utils.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Tìm kiếm và phân trang
    public Page<Account> searchAccounts(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))); // Sắp xếp giảm dần theo id
        return accountRepository.searchAccounts(search, pageable);
    }

    // Lấy tài khoản theo id
    public Optional<Account> getAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    // Tạo tài khoản mới
    public Account createAccount(AccountModel accountModel) {
        // Kiểm tra nếu username hoặc email đã tồn tại
        if (accountRepository.existsByUsername(accountModel.getUsername())) {
            throw new IllegalArgumentException("Tên người dùng đã tồn tại.");
        }
        if (accountRepository.existsByEmail(accountModel.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }

        // Chuyển đổi từ AccountModel sang Account
        Account account = new Account();
        account.setUsername(accountModel.getUsername());
        account.setEmail(accountModel.getEmail());
        account.setPassword(Encode.hashCode(accountModel.getPassword()));

        // Lưu tài khoản vào cơ sở dữ liệu
        return accountRepository.save(account);
    }

    public Account updateAccount(Integer id, AccountModel accountModel) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));

        if (accountRepository.existsByUsernameAndIdNot(accountModel.getUsername(), id)) {
            throw new IllegalArgumentException("Tên người dùng đã tồn tại.");
        }
        if (accountRepository.existsByEmailAndIdNot(accountModel.getEmail(), id)) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }

        existingAccount.setUsername(accountModel.getUsername());
        existingAccount.setEmail(accountModel.getEmail());
        existingAccount.setPassword(Encode.hashCode(accountModel.getPassword()));

        return accountRepository.save(existingAccount);
    }


    // Xóa tài khoản
    public void deleteAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (id == 1) {
            throw new IllegalStateException("Không thể xóa tài khoản hệ thống với ID = 1");
        }

        accountRepository.delete(account);
    }

}
