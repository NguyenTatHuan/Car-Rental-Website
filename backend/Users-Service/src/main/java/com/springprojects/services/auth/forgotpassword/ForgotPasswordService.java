package com.springprojects.services.auth.forgotpassword;

import com.springprojects.dto.forgotpassword.PasswordResetConfirmDto;

public interface ForgotPasswordService {

    void requestResetPassword(String username);

    void confirmResetPassword(PasswordResetConfirmDto dto);

}
