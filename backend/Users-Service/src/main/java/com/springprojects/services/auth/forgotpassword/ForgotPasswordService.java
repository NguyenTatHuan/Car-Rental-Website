package com.springprojects.services.auth.forgotpassword;

import com.springprojects.dto.PasswordResetConfirmDto;

public interface ForgotPasswordService {

    void requestResetPassword(String username);

    void confirmResetPassword(PasswordResetConfirmDto dto);

}
