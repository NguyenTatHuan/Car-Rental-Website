package com.springproject.dto.forgotpassword;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

}
