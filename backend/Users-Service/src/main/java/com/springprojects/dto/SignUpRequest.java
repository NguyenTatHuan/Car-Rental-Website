package com.springprojects.dto;

import com.springprojects.enums.UserGender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequest {

    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$", message = "Username must be 3-20 characters and contain only letters, numbers, underscores or hyphens")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;

    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    @NotBlank(message = "Citizen ID must not be blank")
    @Pattern(regexp = "\\d{9,12}", message = "Citizen ID must be a numeric string with 9 to 12 digits")
    private String citizenID;

    @NotNull(message = "Birthday must not be null")
    @Past(message = "Birthday must be a date in the past")
    private LocalDate birthday;

    @NotNull(message = "Gender must not be null")
    private UserGender gender;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^(0\\d{9})$", message = "Phone number must be a valid 10-digit number starting with 0")
    private String phone;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotBlank(message = "Nationality must not be blank")
    private String nationality;

}
