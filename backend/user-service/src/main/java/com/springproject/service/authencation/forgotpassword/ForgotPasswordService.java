package com.springproject.service.authencation.forgotpassword;

import com.springproject.dto.forgotpassword.PasswordResetConfirm;

public interface ForgotPasswordService {

    void requestResetPassword(String username);

    void confirmResetPassword(PasswordResetConfirm dto);

}
