package com.springproject.service.authencation.twofactorverify;

import com.springproject.dto.login.LoginRequest;
import com.springproject.dto.twofactor.TwoFactorRequest;
import com.springproject.dto.twofactor.TwoFactorResponse;
import com.springproject.entity.User;
import com.springproject.enums.UserRole;
import com.springproject.repository.UserRepository;
import com.springproject.security.JwtService;
import com.springproject.service.email.EmailService;
import com.springproject.service.redis.twofactorverify.OtpRedisTwoFactorService;
import com.springproject.service.redis.twofactorverify.TwoFactorRateLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class TwoFactorVerifyServiceImpl implements TwoFactorVerifyService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final EmailService emailService;

    private final OtpRedisTwoFactorService otpRedisTwoFactorService;

    private final TwoFactorRateLimitService twoFactorRateLimitService;

    private final PasswordEncoder passwordEncoder;

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public void sendTwoFactorOtp(LoginRequest loginRequest) {
        String username = loginRequest.getUsername().trim();
        String password = loginRequest.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with username: " + username));

        if (user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "2FA is only required for ADMIN!");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password!");
        }

        if (twoFactorRateLimitService.isUserLocked(username)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Your account is temporarily locked due to many incorrect OTP attempts.");
        }

        String otp = generateOtp();
        otpRedisTwoFactorService.saveOtp(username, otp);

        try {
            String email = user.getUserInformation().getEmail();
            String fullName = user.getUserInformation().getFullName();
            emailService.sendTwoFactorOtpEmail(email, fullName, otp, 10);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    @Override
    public TwoFactorResponse verifyOtp(TwoFactorRequest dto) {
        String username = dto.getUsername().trim();
        String inputOtp = dto.getOtp().trim();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with username: " + username));

        if (user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "2FA is only required for ADMIN!");
        }

        if (twoFactorRateLimitService.isUserLocked(username)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Your account is locked due to too many failed OTP attempts.");
        }

        boolean isValid = otpRedisTwoFactorService.verifyOtp(username, inputOtp);

        if (!isValid) {
            twoFactorRateLimitService.increaseOtpAttempt(username);
            if (twoFactorRateLimitService.hasExceededMaxAttempts(username)) {
                twoFactorRateLimitService.lockUser(username);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP!");
        }

        twoFactorRateLimitService.clearOtpAttempts(username);
        twoFactorRateLimitService.unlockUser(username);

        String accessToken = jwtService.generateAccessToken(user);

        return new TwoFactorResponse(user.getId(), accessToken, user.getRole());
    }

}
