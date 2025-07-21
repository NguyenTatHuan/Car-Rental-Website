package com.springproject.service.email;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendVerificationOtpEmail(String toEmail, String username, String otp, int expiryMinutes) throws MessagingException;

    void sendResetPasswordOtpEmail(String toEmail, String username, String otp, int expiryMinutes) throws MessagingException;

    void sendTwoFactorOtpEmail(String toEmail, String username, String otp, int expiryMinutes) throws MessagingException;

}
