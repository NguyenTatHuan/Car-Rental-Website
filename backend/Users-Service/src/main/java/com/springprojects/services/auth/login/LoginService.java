package com.springprojects.services.auth.login;

import com.springprojects.dto.LoginRequest;
import com.springprojects.dto.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginRequest loginRequest);

}
