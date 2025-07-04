package com.springprojects.services.auth.signup;

import com.springprojects.dto.SignUpRequest;
import com.springprojects.dto.user.UserDto;
import com.springprojects.dto.userInformation.UserInformationDto;

public interface SignupService {

    UserDto createCustomer(SignUpRequest signupRequest);

    UserInformationDto createInformationCustomer(SignUpRequest signupRequest);

    void validateSignUpRequest(SignUpRequest signupRequest);

}
