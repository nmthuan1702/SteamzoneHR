package com.system.app.services;

import com.system.app.entities.Account;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.exceptions.MessagingException;
import com.system.app.repositories.AccountRepository;
import com.system.app.utils.Contains;
import com.system.app.utils.Encode;
import com.system.app.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    MailService mailService;

    public String login(String username, String plainPassword) {
        Account account = accountRepository.getByUsernameOrEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));

        if (!Encode.checkCode(plainPassword, account.getPassword())) {
            throw new EntityNotFoundException("Mật khẩu không đúng");
        }

        return jwtService.generateToken(account.getUsername());
    }

    public Account validateToken(String token) {
        String username = jwtService.extractUsername(token);
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Token không hợp lệ"));
    }

    public void forgotPassword(String email){
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email không tồn tại trong hệ thống"));

        String token = jwtService.generateResetPasswordToken(account.getUsername());

        String resetLink = Contains.DOMAIN + "/reset-password?token=" + token;
        try {
            mailService.sendResetPasswordMail(account.getUsername(), email, resetLink);
        }
        catch(Exception e) {
            throw new com.system.app.exceptions.MessagingException("Lỗi khi gửi mail");
        }
    }

    public void resetPassword(String token){
        String username = jwtService.extractUsername(token);

        if (!jwtService.validateToken(token, username)) {
            throw new EntityNotFoundException("Token không hợp lệ hoặc đã hết hạn");
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));

        String temporaryPassword = RandomUtil.generateComplexRandomString();

        account.setPassword(Encode.hashCode(temporaryPassword));
        accountRepository.save(account);

        try {
            mailService.sendTemporaryPasswordMail(account.getUsername(), account.getEmail(), temporaryPassword);
        }
        catch(Exception e) {
            throw new MessagingException("Lỗi khi gửi mail cập nhật mật khẩu!");
        }
    }

}
