package com.springprojects.services.auth.forgotpassword;

import com.springprojects.configuration.WebSecurityConfiguration;
import com.springprojects.dto.PasswordResetConfirmDto;
import com.springprojects.entity.PasswordResetToken;
import com.springprojects.entity.User;
import com.springprojects.repository.TokenRepository;
import com.springprojects.repository.UserRepository;
import com.springprojects.services.auth.emailservice.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final EmailService emailService;

    private final WebSecurityConfiguration webSecurityConfiguration;

    private String generateOtp() {
        int otp = (int) (Math.random() * 900_000) + 100_000;
        return String.valueOf(otp);
    }

    @Override
    public void requestResetPassword(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Not found user with username: " + username));

        String otp = generateOtp();
        Optional<PasswordResetToken> existingToken = tokenRepository.findByUser(user);

        PasswordResetToken passwordResetToken = existingToken.orElseGet(PasswordResetToken::new);
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(otp);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(passwordResetToken);

        try {
            String email = user.getUserInformation().getEmail();
            String name = user.getUsername();
            emailService.sendOtpEmail(email, name, otp, 15);
        } catch (MessagingException e) {
            throw new RuntimeException("Error in sending email: " + e.getMessage());
        }
    }

    @Override
    public void confirmResetPassword(PasswordResetConfirmDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new RuntimeException("New Password and Confirm New Password do not match");
        }

        PasswordResetToken token = tokenRepository.findByToken(dto.getToken().trim())
                .orElseThrow(() -> new RuntimeException("Invalid Token!"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("The OTP has expired!");
        }

        User user = token.getUser();
        user.setPassword(webSecurityConfiguration.passwordEncoder().encode(dto.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(token);
    }

}
