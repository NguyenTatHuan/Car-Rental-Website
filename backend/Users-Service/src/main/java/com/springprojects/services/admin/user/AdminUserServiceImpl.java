package com.springprojects.services.admin.user;

import com.springprojects.dto.user.UserDto;
import com.springprojects.dto.user.UserUpdateDto;
import com.springprojects.entity.User;
import com.springprojects.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found user with ID: " + id));
        return entityToDto(user);
    }

    @Override
    public UserDto updateUser(UUID id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found user with ID: " + id));

        if (userUpdateDto.getUsername() != null) {
            if (userRepository.existsByUsernameAndIdNot(userUpdateDto.getUsername(), id)) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(userUpdateDto.getUsername());
        }

        if (userUpdateDto.getRole() != null) {
            user.setRole(userUpdateDto.getRole());
        }

        if (userUpdateDto.getStatus() != null) {
            user.setStatus(userUpdateDto.getStatus());
        }
        return entityToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Not found user with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
