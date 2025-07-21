package com.springproject.service.authencation.twofactorverify;

import com.springproject.dto.login.LoginRequest;
import com.springproject.dto.twofactor.TwoFactorRequest;
import com.springproject.dto.twofactor.TwoFactorResponse;

public interface TwoFactorVerifyService {

    void sendTwoFactorOtp(LoginRequest loginRequest);

    TwoFactorResponse verifyOtp(TwoFactorRequest dto);

}
