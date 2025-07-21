package com.springproject.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "OTP must not be blank")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 digits")
    @Pattern(regexp = "\\d{6}", message = "OTP must contain only digits")
    private String otp;

}
