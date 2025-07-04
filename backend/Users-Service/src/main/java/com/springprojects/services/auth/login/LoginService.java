package com.springprojects.services.auth.login;

import com.springprojects.dto.login.LoginRequest;
import com.springprojects.dto.login.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginRequest loginRequest);

}
