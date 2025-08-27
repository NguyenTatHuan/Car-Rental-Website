package com.springproject.service.admin.user;

import com.springproject.dto.rabbitmq.UserDeletedEvent;
import com.springproject.dto.user.UserDto;
import com.springproject.dto.user.UserUpdateDto;
import com.springproject.elasticsearch.UserDocument;
import com.springproject.entity.User;
import com.springproject.rabbitmq.UserEventProducer;
import com.springproject.repository.UserElasticsearchRepository;
import com.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private final UserEventProducer userEventProducer;

    private final UserElasticsearchRepository userElasticsearchRepository;

    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());
        return userDto;
    }

    private UserDocument mapToDocument(User user) {
        return UserDocument.builder()
                .id(user.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .status(user.getStatus().toString())
                .build();
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with ID: " + id));
        return mapToDto(user);
    }

    @Override
    public UserDto updateUser(UUID id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with ID: " + id));

        if (userUpdateDto.getUsername() != null) {
            if (userRepository.existsByUsernameAndIdNot(userUpdateDto.getUsername(), id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
            }
            user.setUsername(userUpdateDto.getUsername());
        }

        if (userUpdateDto.getRole() != null) {
            user.setRole(userUpdateDto.getRole());
        }

        if (userUpdateDto.getStatus() != null) {
            user.setStatus(userUpdateDto.getStatus());
        }

        User updatedUser = userRepository.save(user);

        try {
            userElasticsearchRepository.save(mapToDocument(updatedUser));
        } catch (Exception e) {
            log.error("Failed to update user to Elasticsearch: {}", e.getMessage());
        }

        return mapToDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with ID: " + id));

        userRepository.deleteById(id);

        try {
            userElasticsearchRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete user to Elasticsearch: {}", e.getMessage());
        }

        UserDeletedEvent event = new UserDeletedEvent(user.getId());
        userEventProducer.sendUserDeletedEvent(event);
    }

}