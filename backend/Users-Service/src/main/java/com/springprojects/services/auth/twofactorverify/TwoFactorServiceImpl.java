package com.springprojects.services.auth.twofactorverify;

import com.springprojects.dto.LoginRequest;
import com.springprojects.dto.TwoFactorRequestDto;
import com.springprojects.dto.TwoFactorResponseDto;
import com.springprojects.entity.TwoFactorToken;
import com.springprojects.entity.User;
import com.springprojects.enums.UserRole;
import com.springprojects.repository.TwoFactorTokenRepository;
import com.springprojects.repository.UserRepository;
import com.springprojects.services.auth.emailservice.EmailService;
import com.springprojects.services.jwt.UserService;
import com.springprojects.utils.JWTUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TwoFactorServiceImpl implements TwoFactorService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final TwoFactorTokenRepository twoFactorTokenRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    private static final int OTP_EXPIRE_MINUTES = 5;

    @Override
    @Transactional
    public void sendTwoFactorOtp(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Not found user with username: " + loginRequest.getUsername()));

        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("2FA is only required for admin users!");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password!");
        }

        String otp = String.format("%06d", new Random().nextInt(1000000));

        Optional<TwoFactorToken> existingToken = twoFactorTokenRepository.findByUser(user);

        TwoFactorToken twoFactorToken = existingToken.orElseGet(TwoFactorToken::new);
        twoFactorToken.setUser(user);
        twoFactorToken.setOtp(otp);
        twoFactorToken.setCreatedAt(LocalDateTime.now());
        twoFactorToken.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES));
        twoFactorToken.setVerified(false);

        twoFactorTokenRepository.save(twoFactorToken);

        try {
            emailService.sendTwoFactorOtpEmail(
                    user.getUserInformation().getEmail(),
                    user.getUsername(),
                    otp,
                    OTP_EXPIRE_MINUTES
            );
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    @Override
    @Transactional
    public TwoFactorResponseDto verifyOtp(TwoFactorRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Not found user with username: " + dto.getUsername()));

        Optional<TwoFactorToken> tokenOpt = twoFactorTokenRepository.findByUser(user);

        if (tokenOpt.isEmpty()) {
            return buildResponse(user, null, "OTP not found.");
        }

        TwoFactorToken token = tokenOpt.get();

        if (!token.getOtp().equals(dto.getOtp())) {
            return buildResponse(user, null, "Invalid OTP.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            return buildResponse(user, null, "OTP has expired.");
        }

        token.setVerified(true);
        twoFactorTokenRepository.save(token);

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        return buildResponse(user, jwt, "OTP verified successfully!");
    }

    private TwoFactorResponseDto buildResponse(User user, String jwt, String message) {
        return TwoFactorResponseDto.builder()
                .id(user.getId())
                .jwt(jwt)
                .userRole(user.getRole())
                .message(message)
                .build();
    }

}
