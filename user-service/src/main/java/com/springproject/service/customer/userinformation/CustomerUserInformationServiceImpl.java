package com.springproject.service.customer.userinformation;

import com.springproject.dto.rabbitmq.UserUpdatedEvent;
import com.springproject.dto.userinformation.UserInformationUpdateDto;
import com.springproject.elasticsearch.UserInformationDocument;
import com.springproject.entity.User;
import com.springproject.entity.UserInformation;
import com.springproject.rabbitmq.UserEventProducer;
import com.springproject.repository.UserInformationElasticsearchRepository;
import com.springproject.repository.UserInformationRepository;
import com.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerUserInformationServiceImpl implements CustomerUserInformationService {

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    private final UserEventProducer userEventProducer;

    private final UserInformationElasticsearchRepository userInformationElasticsearchRepository;

    private UserInformationUpdateDto mapToDto(UserInformation userInformation) {
        return UserInformationUpdateDto.builder()
                .fullName(userInformation.getFullName())
                .citizenID(userInformation.getCitizenID())
                .birthday(userInformation.getBirthday())
                .gender(userInformation.getGender())
                .email(userInformation.getEmail())
                .phone(userInformation.getPhone())
                .address(userInformation.getAddress())
                .nationality(userInformation.getNationality())
                .build();
    }

    private UserInformationDocument mapToDocument(UserInformation userInformation) {
        return UserInformationDocument.builder()
                .id(userInformation.getUser().getId())
                .userId(userInformation.getUser().getId())
                .userInformationId(userInformation.getId())
                .fullName(userInformation.getFullName())
                .citizenID(userInformation.getCitizenID())
                .birthday(userInformation.getBirthday() != null ? userInformation.getBirthday().toString() : null)
                .gender(userInformation.getGender() != null ? userInformation.getGender().toString() : null)
                .email(userInformation.getEmail())
                .phone(userInformation.getPhone())
                .address(userInformation.getAddress())
                .nationality(userInformation.getNationality())
                .build();
    }

    @Override
    public UserInformationUpdateDto getMyInformation(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserInformation info = userInformationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user information found"));

        return mapToDto(info);
    }

    @Override
    public UserInformationUpdateDto updateMyInformation(UUID userId, UserInformationUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserInformation userInformation = userInformationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user information found"));

        boolean fullNameChanged = false;

        if (dto.getEmail() != null) {
            if (!dto.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is not valid");
            }
            var existing = userInformationRepository.findFirstByEmail(dto.getEmail());
            if (existing.isPresent() && !existing.get().getId().equals(userInformation.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }
            userInformation.setEmail(dto.getEmail());
        }

        if (dto.getCitizenID() != null) {
            if (!dto.getCitizenID().matches("\\d{9,12}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Citizen ID must be 9 to 12 digits");
            }
            var existing = userInformationRepository.findFirstByCitizenID(dto.getCitizenID());
            if (existing.isPresent() && !existing.get().getId().equals(userInformation.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Citizen ID already in use");
            }
            userInformation.setCitizenID(dto.getCitizenID());
        }

        if (dto.getPhone() != null) {
            if (!dto.getPhone().matches("^0\\d{9}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must be a valid 10-digit number starting with 0");
            }
            var existing = userInformationRepository.findFirstByPhone(dto.getPhone());
            if (existing.isPresent() && !existing.get().getId().equals(userInformation.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already in use");
            }
            userInformation.setPhone(dto.getPhone());
        }

        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            if (!dto.getFullName().equals(userInformation.getFullName())) {
                fullNameChanged = true;
                userInformation.setFullName(dto.getFullName());
            }
        }

        if (dto.getBirthday() != null) {
            if (dto.getBirthday().isAfter(java.time.LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthday must be in the past");
            }
            userInformation.setBirthday(dto.getBirthday());
        }

        if (dto.getGender() != null) {
            userInformation.setGender(dto.getGender());
        }

        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            userInformation.setAddress(dto.getAddress());
        }

        if (dto.getNationality() != null && !dto.getNationality().isBlank()) {
            userInformation.setNationality(dto.getNationality());
        }

        userInformationRepository.save(userInformation);

        try {
            userInformationElasticsearchRepository.save(mapToDocument(userInformation));
        } catch (Exception e) {
            log.error("Failed to save user to Elasticsearch: {}", e.getMessage());
        }

        if (fullNameChanged) {
            UserUpdatedEvent event = new UserUpdatedEvent(
                    user.getId(),
                    user.getUsername(),
                    userInformation.getFullName()
            );
            userEventProducer.sendUserUpdatedEvent(event);
        }

        return mapToDto(userInformation);
    }

}
