package com.springprojects.dto;

import com.springprojects.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TwoFactorResponseDto {

    private UUID id;

    private String jwt;

    private UserRole userRole;

    private String message;

}
