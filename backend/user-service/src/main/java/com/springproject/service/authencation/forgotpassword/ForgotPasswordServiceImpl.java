package com.springproject.service.authencation.forgotpassword;

import com.springproject.dto.forgotpassword.PasswordResetConfirm;
import com.springproject.entity.User;
import com.springproject.repository.UserRepository;
import com.springproject.service.email.EmailService;
import com.springproject.service.redis.forgotpassword.ForgotRateLimitService;
import com.springproject.service.redis.forgotpassword.OtpRedisForgotService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;

    private final ForgotRateLimitService forgotRateLimitService;

    private final OtpRedisForgotService otpRedisForgotService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    @Override
    public void requestResetPassword(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with username: " + username));

        String otp = generateOtp();
        otpRedisForgotService.saveOtp(username, otp);
        forgotRateLimitService.resetAttempts(username);

        try {
            String email = user.getUserInformation().getEmail();
            String fullName = user.getUserInformation().getFullName();
            emailService.sendResetPasswordOtpEmail(email, fullName, otp, 10);
        } catch (MessagingException e) {
            throw new RuntimeException("Error in sending email: " + e.getMessage());
        }
    }

    @Override
    public void confirmResetPassword(PasswordResetConfirm dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Password and Confirm New Password do not match!");
        }

        if (forgotRateLimitService.isBlocked(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many incorrect OTP attempts. Please request a new OTP.");
        }

        boolean isValid = otpRedisForgotService.verifyOtp(dto.getUsername(), dto.getToken().trim());

        if (!isValid) {
            forgotRateLimitService.increaseAttempts(dto.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP!");
        }

        forgotRateLimitService.resetAttempts(dto.getUsername());

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with username: " + dto.getUsername()));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
