package com.springprojects.services.auth.signup;

import com.springprojects.configuration.WebSecurityConfiguration;
import com.springprojects.dto.SignUpRequest;
import com.springprojects.dto.user.UserDto;
import com.springprojects.dto.userInformation.UserInformationDto;
import com.springprojects.entity.User;
import com.springprojects.entity.UserInformation;
import com.springprojects.enums.UserRole;
import com.springprojects.enums.UserStatus;
import com.springprojects.repository.UserInformationRepository;
import com.springprojects.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    private final WebSecurityConfiguration webSecurityConfiguration;

    @Override
    public UserDto createCustomer(SignUpRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(webSecurityConfiguration.passwordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        User createdUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        userDto.setUsername(createdUser.getUsername());
        userDto.setRole(createdUser.getRole());
        userDto.setStatus(createdUser.getStatus());
        return userDto;
    }

    @Transactional
    @Override
    public UserInformationDto createInformationCustomer(SignUpRequest signupRequest) {
        User user = userRepository.findByUsername(signupRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserInformation userInformation = new UserInformation();
        userInformation.setFullName(signupRequest.getFullName());
        userInformation.setCitizenID(signupRequest.getCitizenID());
        userInformation.setBirthday(signupRequest.getBirthday());
        userInformation.setGender(signupRequest.getGender());
        userInformation.setEmail(signupRequest.getEmail());
        userInformation.setPhone(signupRequest.getPhone());
        userInformation.setAddress(signupRequest.getAddress());
        userInformation.setNationality(signupRequest.getNationality());
        userInformation.setUser(user);

        UserInformation createdUserInformation = userInformationRepository.save(userInformation);

        UserInformationDto userInformationDto = new UserInformationDto();
        userInformationDto.setId(createdUserInformation.getId());
        userInformationDto.setUserId(createdUserInformation.getUser().getId());
        userInformationDto.setFullName(createdUserInformation.getFullName());
        userInformationDto.setCitizenID(createdUserInformation.getCitizenID());
        userInformationDto.setBirthday(createdUserInformation.getBirthday());
        userInformationDto.setGender(createdUserInformation.getGender());
        userInformationDto.setEmail(createdUserInformation.getEmail());
        userInformationDto.setPhone(createdUserInformation.getPhone());
        userInformationDto.setAddress(createdUserInformation.getAddress());
        userInformationDto.setNationality(createdUserInformation.getNationality());
        userInformationDto.setCreatedAt(createdUserInformation.getCreatedAt());
        userInformationDto.setUpdatedAt(createdUserInformation.getUpdatedAt());
        return userInformationDto;
    }

    @Override
    public void validateSignUpRequest(SignUpRequest signupRequest) {
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password do not match");
        }

        if (userInformationRepository.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Customer already exists with this email");
        }

        if (userInformationRepository.findFirstByCitizenID(signupRequest.getCitizenID()).isPresent()) {
            throw new IllegalArgumentException("Customer already exists with this citizen ID");
        }

        if (userInformationRepository.findFirstByPhone(signupRequest.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Customer already exists with this phone number");
        }

        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Customer already exists with this username");
        }
    }

}
