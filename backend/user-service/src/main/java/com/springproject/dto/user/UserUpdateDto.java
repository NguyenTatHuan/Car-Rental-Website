package com.springproject.dto.user;

import com.springproject.enums.UserRole;
import com.springproject.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String username;

    private UserRole role;

    private UserStatus status;

}
