package com.springproject.service.authencation.signup;

import com.springproject.dto.signup.SignUpRequest;

public interface SignupService {

    void createCustomer(SignUpRequest signupRequest, String ipAddress);

    void validateSignUpRequest(SignUpRequest signupRequest);

    void verifyEmailOtp(String email, String otp);

}
