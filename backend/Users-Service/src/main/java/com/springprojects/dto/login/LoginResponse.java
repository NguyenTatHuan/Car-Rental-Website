package com.springprojects.dto.login;

import com.springprojects.enums.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginResponse {

    private UUID id;

    private String jwt;

    private UserRole userRole;

}
