package com.springproject.service.authencation.signup;

import com.springproject.dto.signup.SignUpRequest;
import com.springproject.entity.User;
import com.springproject.entity.UserInformation;
import com.springproject.enums.UserRole;
import com.springproject.enums.UserStatus;
import com.springproject.repository.UserInformationRepository;
import com.springproject.repository.UserRepository;
import com.springproject.service.email.EmailService;
import com.springproject.service.redis.signup.IpRateLimitService;
import com.springproject.service.redis.signup.OtpRedisService;
import com.springproject.service.redis.signup.SignupRateLimitService;
import com.springproject.service.redis.signup.SignupRequestRedisService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final SignupRequestRedisService signupRequestRedisService;

    private final OtpRedisService otpRedisService;

    private final SignupRateLimitService signupRateLimitService;

    private final IpRateLimitService ipRateLimitService;

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    @Transactional
    @Override
    public void createCustomer(SignUpRequest signupRequest, String ipAddress) {
        validateSignUpRequest(signupRequest);
        String email = signupRequest.getEmail().trim().toLowerCase();

        if (ipRateLimitService.isBlocked(ipAddress)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many OTP requests from your IP. Please try again later!");
        }

        if (signupRateLimitService.isBlocked(email)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many OTP requests. Please try again later!");
        }

        String otp = generateOtp();
        otpRedisService.saveOtp(email, otp);
        signupRequestRedisService.saveRequest(email, signupRequest);

        try {
            emailService.sendVerificationOtpEmail(
                    email,
                    signupRequest.getFullName(),
                    otp,
                    10
            );
            signupRateLimitService.increaseAttempts(email);
            ipRateLimitService.increaseAttempts(ipAddress);
        } catch (MessagingException e) {
            otpRedisService.deleteOtp(email);
            signupRequestRedisService.deleteRequest(email);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send verification email!");
        }
    }

    @Override
    public void validateSignUpRequest(SignUpRequest signupRequest) {
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and Confirm Password do not match");
        }

        if (userInformationRepository.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this email");
        }

        if (userInformationRepository.findFirstByCitizenID(signupRequest.getCitizenID()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this citizen ID");
        }

        if (userInformationRepository.findFirstByPhone(signupRequest.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this phone number");
        }

        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this username");
        }
    }

    @Transactional
    @Override
    public void verifyEmailOtp(String email, String otp) {
        email = email.trim().toLowerCase();
        SignUpRequest request = signupRequestRedisService.getRequestByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No signup info found or expired!"));

        if (!otpRedisService.verifyOtp(email, otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        User createdUser = userRepository.save(user);

        UserInformation info = new UserInformation();
        info.setFullName(request.getFullName());
        info.setCitizenID(request.getCitizenID());
        info.setBirthday(request.getBirthday());
        info.setGender(request.getGender());
        info.setEmail(request.getEmail());
        info.setPhone(request.getPhone());
        info.setAddress(request.getAddress());
        info.setNationality(request.getNationality());
        info.setUser(createdUser);
        userInformationRepository.save(info);

        signupRateLimitService.resetAttempts(email);
        signupRequestRedisService.deleteRequest(email);
    }
}
