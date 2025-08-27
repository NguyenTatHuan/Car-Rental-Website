package com.springproject.service.customer.user;

import com.springproject.dto.user.ChangePasswordDto;

import java.util.UUID;

public interface CustomerUserService {

    void changePassword(UUID userId, ChangePasswordDto changePasswordDto);

}
