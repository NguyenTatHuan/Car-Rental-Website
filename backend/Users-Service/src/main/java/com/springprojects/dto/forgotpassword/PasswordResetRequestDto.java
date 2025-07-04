package com.springprojects.dto.forgotpassword;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDto {

    @NotBlank(message = "Username must not be blank")
    private String username;

}
