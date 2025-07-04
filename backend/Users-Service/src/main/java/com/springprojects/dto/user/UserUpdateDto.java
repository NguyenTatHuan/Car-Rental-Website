package com.springprojects.dto.user;

import com.springprojects.enums.UserRole;
import com.springprojects.enums.UserStatus;
import lombok.Data;

@Data
public class UserUpdateDto {

    private String username;

    private UserRole role;

    private UserStatus status;

}
