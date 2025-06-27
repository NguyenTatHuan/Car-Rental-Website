package com.springprojects.services.auth.login;

import com.springprojects.configuration.WebSecurityConfiguration;
import com.springprojects.dto.LoginRequest;
import com.springprojects.dto.LoginResponse;
import com.springprojects.entity.User;
import com.springprojects.enums.UserRole;
import com.springprojects.repository.UserInformationRepository;
import com.springprojects.repository.UserRepository;
import com.springprojects.services.jwt.UserService;
import com.springprojects.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JWTUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws
            BadCredentialsException,
            DisabledException,
            UsernameNotFoundException {

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with username: " + loginRequest.getUsername()));

        if (user.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("Only customers are allowed to login directly!");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (DisabledException e) {
            throw new DisabledException("User account is disabled");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect Username or Password");
        }

        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(loginRequest.getUsername());
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        LoginResponse loginResponse = new LoginResponse();
        if (optionalUser.isPresent()) {
            loginResponse.setJwt(jwt);
            loginResponse.setId(optionalUser.get().getId());
            loginResponse.setUserRole(optionalUser.get().getRole());
        }
        return loginResponse;
    }

}
