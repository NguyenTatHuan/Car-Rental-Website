package com.springproject.dto.twofactor;

import com.springproject.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorResponse {

    private UUID id;

    private String jwt;

    private UserRole userRole;

}
