package com.springprojects.services.admin.user;

import com.springprojects.dto.UserDto;
import com.springprojects.dto.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminUserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(UUID id);

    UserDto updateUser(UUID id, UserUpdateDto userUpdateDto);

    void deleteUser(UUID id);

}
