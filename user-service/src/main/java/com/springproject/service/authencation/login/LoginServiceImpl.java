package com.springproject.service.authencation.login;

import com.springproject.dto.login.LoginRequest;
import com.springproject.dto.login.LoginResponse;
import com.springproject.entity.User;
import com.springproject.enums.UserRole;
import com.springproject.enums.UserStatus;
import com.springproject.repository.UserRepository;
import com.springproject.security.JwtService;
import com.springproject.service.redis.login.LoginRateLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final LoginRateLimitService loginRateLimitService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws
            BadCredentialsException,
            DisabledException,
            UsernameNotFoundException {

        String username = loginRequest.getUsername().trim();

        if (loginRateLimitService.isBlocked(username)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Your account has been temporarily locked due to too many failed login attempts. Please try again later!");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user with username: " + username));

        if (user.getRole() != UserRole.CUSTOMER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers are allowed to login directly!");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is not active!");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword())
            );
        } catch (DisabledException e) {
            throw new DisabledException("User account is disabled!");
        } catch (BadCredentialsException e) {
            loginRateLimitService.incrementFailedAttempts(username);
            throw new BadCredentialsException("Incorrect Username or Password!");
        }

        loginRateLimitService.clearAttempts(username);

        final String jwt = jwtService.generateAccessToken(user);

        return new LoginResponse(user.getId(), jwt, user.getRole());

    }
}
