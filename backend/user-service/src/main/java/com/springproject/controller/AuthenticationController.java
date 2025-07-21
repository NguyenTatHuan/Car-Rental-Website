package com.springproject.controller;

import com.springproject.dto.forgotpassword.PasswordResetConfirm;
import com.springproject.dto.forgotpassword.PasswordResetRequest;
import com.springproject.dto.login.LoginRequest;
import com.springproject.dto.login.LoginResponse;
import com.springproject.dto.signup.EmailVerificationRequest;
import com.springproject.dto.signup.SignUpRequest;
import com.springproject.dto.twofactor.TwoFactorRequest;
import com.springproject.dto.twofactor.TwoFactorResponse;
import com.springproject.service.authencation.forgotpassword.ForgotPasswordService;
import com.springproject.service.authencation.login.LoginService;
import com.springproject.service.authencation.logout.LogoutService;
import com.springproject.service.authencation.signup.SignupService;
import com.springproject.service.authencation.twofactorverify.TwoFactorVerifyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final SignupService signupService;

    private final LoginService loginService;

    private final ForgotPasswordService forgotPasswordService;

    private final TwoFactorVerifyService twoFactorVerifyService;

    private final LogoutService logoutService;

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpCustomer(@Valid @RequestBody SignUpRequest signupRequest, HttpServletRequest httpRequest) {
        String ipAddress = getClientIp(httpRequest);
        signupService.createCustomer(signupRequest, ipAddress);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "OTP sent to your email. Please verify to activate your account!"));
    }

    @PostMapping("/signup/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
        signupService.verifyEmailOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok("Email verified successfully. You can now log in!");
    }

    @PostMapping("/login")
    public LoginResponse loginCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody PasswordResetRequest requestDto) {
        forgotPasswordService.requestResetPassword(requestDto.getUsername());
        return ResponseEntity.ok("OTP has been sent to your registered email!");
    }

    @PostMapping("/forgot-password/confirm")
    public ResponseEntity<?> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirm confirmDto) {
        forgotPasswordService.confirmResetPassword(confirmDto);
        return ResponseEntity.ok("Password has been successfully reset!");
    }

    @PostMapping("/2fa/send-otp")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        twoFactorVerifyService.sendTwoFactorOtp(loginRequest);
        return ResponseEntity.ok("OTP has been sent to your email!");
    }

    @PostMapping("/2fa/verify-otp")
    public ResponseEntity<?> verify2faOtp(@Valid @RequestBody TwoFactorRequest requestDto) {
        TwoFactorResponse responseDto = twoFactorVerifyService.verifyOtp(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok("Logout successfully!");
    }

}
