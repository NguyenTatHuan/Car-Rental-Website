package com.springprojects.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TwoFactorRequestDto {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "OTP must not be blank")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 digits")
    @Pattern(regexp = "\\d{6}", message = "OTP must contain only digits")
    private String otp;

}
