package com.system.app.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendHtmlMail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendTemporaryPasswordMail(String username, String to, String temporaryPassword) throws MessagingException {
        String subject = "Mật Khẩu Tạm Thời Của Bạn";
        String htmlBody = "<p>Chào " + username + ",</p>"
                + "<p>Theo yêu cầu, chúng tôi đã tạo một mật khẩu tạm thời cho tài khoản của bạn:</p>"
                + "<p><strong>" + temporaryPassword + "</strong></p>"
                + "<p>Vui lòng sử dụng mật khẩu này để đăng nhập và đổi mật khẩu ngay lập tức.</p>"
                + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng liên hệ với bộ phận hỗ trợ của chúng tôi.</p>"
                + "<p>Xin cảm ơn.</p>"
                + "<p>Trân trọng,<br> Endless</p>";

        sendHtmlMail(to, subject, htmlBody);
    }

    public void sendResetPasswordMail(String username, String to, String resetLink) throws MessagingException {
        String subject = "Yêu Cầu Đặt Lại Mật Khẩu";
        String htmlBody = "<p>Chào " + username + ",</p>"
                + "<p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>"
                + "<p>Nếu bạn thực hiện yêu cầu này, vui lòng nhấp vào liên kết dưới đây để đặt lại mật khẩu của bạn:</p>"
                + "<p><a href=\"" + resetLink + "\">Đặt Lại Mật Khẩu</a></p>"
                + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này hoặc liên hệ với bộ phận hỗ trợ của chúng tôi.</p>"
                + "<p>Xin cảm ơn.</p>"
                + "<p>Trân trọng,<br> Endless</p>";

        sendHtmlMail(to, subject, htmlBody);
    }
}
