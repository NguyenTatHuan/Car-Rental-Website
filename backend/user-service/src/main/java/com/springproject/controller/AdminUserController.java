package com.springproject.controller;

import com.springproject.dto.user.UserDto;
import com.springproject.dto.user.UserUpdateDto;
import com.springproject.service.admin.user.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ) {
        return ResponseEntity.ok(adminUserService.updateUser(id, userUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
