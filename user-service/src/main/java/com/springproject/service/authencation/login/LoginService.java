package com.springproject.service.authencation.login;

import com.springproject.dto.login.LoginRequest;
import com.springproject.dto.login.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginRequest loginRequest);

}
