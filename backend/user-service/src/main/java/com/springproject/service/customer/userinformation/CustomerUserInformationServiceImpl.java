package com.springproject.service.customer.userinformation;

import com.springproject.dto.userinformation.UserInformationUpdateDto;
import com.springproject.entity.User;
import com.springproject.entity.UserInformation;
import com.springproject.repository.UserInformationRepository;
import com.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerUserInformationServiceImpl implements CustomerUserInformationService {

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }

    private User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private UserInformationUpdateDto entityToDto(UserInformation userInformation) {
        UserInformationUpdateDto dto = new UserInformationUpdateDto();
        dto.setFullName(userInformation.getFullName());
        dto.setCitizenID(userInformation.getCitizenID());
        dto.setBirthday(userInformation.getBirthday());
        dto.setGender(userInformation.getGender());
        dto.setEmail(userInformation.getEmail());
        dto.setPhone(userInformation.getPhone());
        dto.setAddress(userInformation.getAddress());
        dto.setNationality(userInformation.getNationality());
        return dto;
    }

    @Override
    public UserInformationUpdateDto getMyInformation() {
        User user = getCurrentUser();
        UserInformation info = userInformationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user information found"));
        return entityToDto(info);
    }

    @Override
    public UserInformationUpdateDto updateMyInformation(UserInformationUpdateDto dto) {
        User user = getCurrentUser();
        UserInformation userInformation = userInformationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user information found"));

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
            userInformation.setFullName(dto.getFullName());
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
        return entityToDto(userInformation);
    }

}
