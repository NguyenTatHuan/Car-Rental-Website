package com.springproject.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    public void sendVerificationOtpEmail(String toEmail, String fullName, String otp, int expiryMinutes) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("otp", otp);
        context.setVariable("expiryMinutes", expiryMinutes);

        String htmlContent = templateEngine.process("otp_email_verification", context);

        helper.setTo(toEmail);
        helper.setSubject("OTP Code for Email Verification");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendResetPasswordOtpEmail(String toEmail, String fullName, String otp, int expiryMinutes) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("otp", otp);
        context.setVariable("expiryMinutes", expiryMinutes);

        String htmlContent = templateEngine.process("otp_email_forgot_password", context);

        helper.setTo(toEmail);
        helper.setSubject("OTP Code for Reset Password");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendTwoFactorOtpEmail(String toEmail, String fullName, String otp, int expiryMinutes) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("otp", otp);
        context.setVariable("expiryMinutes", expiryMinutes);

        String htmlContent = templateEngine.process("otp_email_two_factor", context);

        helper.setTo(toEmail);
        helper.setSubject("OTP Code for 2FA VERIFY");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

}
