package com.springprojects.dto;

import com.springprojects.enums.UserRole;
import com.springprojects.enums.UserStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    private String username;

    private UserRole role;

    private UserStatus status;

}
