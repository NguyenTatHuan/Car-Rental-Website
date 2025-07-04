package com.springprojects.services.auth.twofactorverify;

import com.springprojects.dto.login.LoginRequest;
import com.springprojects.dto.twofactor.TwoFactorRequestDto;
import com.springprojects.dto.twofactor.TwoFactorResponseDto;

public interface TwoFactorService {

    void sendTwoFactorOtp(LoginRequest loginRequest);

    TwoFactorResponseDto verifyOtp(TwoFactorRequestDto dto);

}
