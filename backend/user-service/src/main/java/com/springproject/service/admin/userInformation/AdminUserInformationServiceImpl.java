package com.springproject.service.admin.userInformation;

import com.springproject.dto.rabbitmq.UserDeletedEvent;
import com.springproject.dto.rabbitmq.UserUpdatedEvent;
import com.springproject.dto.userinformation.UserInformationDto;
import com.springproject.dto.userinformation.UserInformationUpdateDto;
import com.springproject.entity.UserInformation;
import com.springproject.rabbitmq.UserEventProducer;
import com.springproject.repository.UserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserInformationServiceImpl implements AdminUserInformationService {

    private final UserInformationRepository userInformationRepository;

    private final UserEventProducer userEventProducer;

    private UserInformationDto entityToDto(UserInformation userInformation) {
        UserInformationDto userInformationDto = new UserInformationDto();
        userInformationDto.setId(userInformation.getId());
        userInformationDto.setUserId(userInformation.getUser().getId());
        userInformationDto.setFullName(userInformation.getFullName());
        userInformationDto.setCitizenID(userInformation.getCitizenID());
        userInformationDto.setBirthday(userInformation.getBirthday());
        userInformationDto.setGender(userInformation.getGender());
        userInformationDto.setEmail(userInformation.getEmail());
        userInformationDto.setPhone(userInformation.getPhone());
        userInformationDto.setAddress(userInformation.getAddress());
        userInformationDto.setNationality(userInformation.getNationality());
        userInformationDto.setCreatedAt(userInformation.getCreatedAt());
        userInformationDto.setUpdatedAt(userInformation.getUpdatedAt());
        return userInformationDto;
    }

    @Override
    public List<UserInformationDto> getAllUserInformation() {
        return userInformationRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserInformationDto getUserInformationById(UUID id) {
        UserInformation userInformation = userInformationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not found user information with ID: " + id));
        return entityToDto(userInformation);
    }

    @Override
    public UserInformationDto updateUser(UUID id, UserInformationUpdateDto dto) {
        UserInformation userInformation = userInformationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user information with ID: " + id));

        boolean fullNameUpdated = false;
        String oldFullName = userInformation.getFullName();

        if (dto.getEmail() != null) {
            if (!dto.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is not valid");
            }
            var existing = userInformationRepository.findFirstByEmail(dto.getEmail());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this email");
            }
            userInformation.setEmail(dto.getEmail());
        }

        if (dto.getCitizenID() != null) {
            if (!dto.getCitizenID().matches("\\d{9,12}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Citizen ID must be 9 to 12 digits");
            }
            var existing = userInformationRepository.findFirstByCitizenID(dto.getCitizenID());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this citizen ID");
            }
            userInformation.setCitizenID(dto.getCitizenID());
        }

        if (dto.getPhone() != null) {
            if (!dto.getPhone().matches("^0\\d{9}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must be a 10-digit number starting with 0");
            }
            var existing = userInformationRepository.findFirstByPhone(dto.getPhone());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this phone number");
            }
            userInformation.setPhone(dto.getPhone());
        }

        if (dto.getFullName() != null) {
            if (dto.getFullName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name must not be blank");
            }
            if (!dto.getFullName().equals(oldFullName)) {
                userInformation.setFullName(dto.getFullName());
                fullNameUpdated = true;
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

        if (dto.getAddress() != null) {
            if (dto.getAddress().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address must not be blank");
            }
            userInformation.setAddress(dto.getAddress());
        }

        if (dto.getNationality() != null) {
            if (dto.getNationality().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nationality must not be blank");
            }
            userInformation.setNationality(dto.getNationality());
        }

        userInformationRepository.save(userInformation);

        if (fullNameUpdated) {
            UserUpdatedEvent event = new UserUpdatedEvent();
            event.setUserId(userInformation.getUser().getId());
            event.setNewUsername(userInformation.getUser().getUsername());
            event.setNewFullName(userInformation.getFullName());
            userEventProducer.sendUserUpdatedEvent(event);
        }

        return entityToDto(userInformation);
    }

    @Override
    public void deleteUser(UUID id) {
        UserInformation userInformation = userInformationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user information with ID: " + id));
        userInformationRepository.delete(userInformation);

        UserDeletedEvent event = new UserDeletedEvent();
        event.setUserId(userInformation.getUser().getId());
        userEventProducer.sendUserDeletedEvent(event);
    }

}
