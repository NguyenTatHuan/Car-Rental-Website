package com.springprojects.services.auth.logout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LogoutService {

    void logout(HttpServletRequest request, HttpServletResponse response);

}
