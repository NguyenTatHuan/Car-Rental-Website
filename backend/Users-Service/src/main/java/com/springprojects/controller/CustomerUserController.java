package com.springprojects.controller;

import com.springprojects.dto.ChangePasswordDto;
import com.springprojects.services.customer.user.CustomerUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/user")
@RequiredArgsConstructor
public class CustomerUserController {

    private final CustomerUserService customerUserService;

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDto changePasswordDto,
            Authentication authentication) {
        String username = authentication.getName();
        UUID userId = customerUserService.getUserIdByUsername(username);
        customerUserService.changePassword(userId, changePasswordDto);
        return ResponseEntity.ok("Password changed successfully!");
    }

}
