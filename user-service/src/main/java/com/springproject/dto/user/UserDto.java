package com.springproject.dto.user;

import com.springproject.enums.UserRole;
import com.springproject.enums.UserStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;

    private String username;

    private UserRole role;

    private UserStatus status;

}
