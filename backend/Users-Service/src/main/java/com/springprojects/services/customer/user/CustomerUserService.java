package com.springprojects.services.customer.user;

import com.springprojects.dto.ChangePasswordDto;

import java.util.UUID;

public interface CustomerUserService {

    UUID getUserIdByUsername(String username);

    void changePassword(UUID userId, ChangePasswordDto changePasswordDto);

}
