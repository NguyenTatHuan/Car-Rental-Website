package com.springprojects.dto;

import com.springprojects.enums.UserRole;
import com.springprojects.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDto {

    private String username;

    private UserRole role;

    private UserStatus status;

}
