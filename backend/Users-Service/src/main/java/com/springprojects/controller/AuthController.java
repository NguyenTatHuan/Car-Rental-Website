package com.springprojects.controller;

import com.springprojects.dto.*;
import com.springprojects.dto.forgotpassword.PasswordResetConfirmDto;
import com.springprojects.dto.forgotpassword.PasswordResetRequestDto;
import com.springprojects.dto.login.LoginRequest;
import com.springprojects.dto.login.LoginResponse;
import com.springprojects.dto.twofactor.TwoFactorRequestDto;
import com.springprojects.dto.twofactor.TwoFactorResponseDto;
import com.springprojects.dto.user.UserDto;
import com.springprojects.dto.userInformation.UserInformationDto;
import com.springprojects.services.auth.forgotpassword.ForgotPasswordService;
import com.springprojects.services.auth.login.LoginService;
import com.springprojects.services.auth.logout.LogoutService;
import com.springprojects.services.auth.signup.SignupService;
import com.springprojects.services.auth.twofactorverify.TwoFactorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    private final LoginService loginService;

    private final LogoutService logoutService;

    private final ForgotPasswordService forgotPasswordService;

    private final TwoFactorService twoFactorService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@Valid @RequestBody SignUpRequest signupRequest) {
        try {
            signupService.validateSignUpRequest(signupRequest);

            UserDto userDto = signupService.createCustomer(signupRequest);
            UserInformationDto userInformationDto = signupService.createInformationCustomer(signupRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("user", userDto);
            response.put("userInformation", userInformationDto);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok("Logout successfully!");
    }

    @PostMapping("/request-forgot-password")
    public ResponseEntity<?> requestResetPassword(@Valid @RequestBody PasswordResetRequestDto requestDto) {
        try {
            forgotPasswordService.requestResetPassword(requestDto.getUsername());
            return ResponseEntity.ok("OTP has been sent to your registered email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/reset-forgot-password")
    public ResponseEntity<?> confirmResetPassword(@Valid @RequestBody PasswordResetConfirmDto confirmDto) {
        try {
            forgotPasswordService.confirmResetPassword(confirmDto);
            return ResponseEntity.ok("Password has been successfully reset.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reset password: " + e.getMessage());
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            twoFactorService.sendTwoFactorOtp(loginRequest);
            return ResponseEntity.ok("OTP has been sent to your email!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody TwoFactorRequestDto requestDto) {
        try {
            TwoFactorResponseDto responseDto = twoFactorService.verifyOtp(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Failed to verify OTP: " + e.getMessage());
        }
    }

}
