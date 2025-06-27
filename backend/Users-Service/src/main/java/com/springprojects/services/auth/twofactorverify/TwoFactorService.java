package com.springprojects.services.auth.twofactorverify;

import com.springprojects.dto.LoginRequest;
import com.springprojects.dto.TwoFactorRequestDto;
import com.springprojects.dto.TwoFactorResponseDto;

public interface TwoFactorService {

    void sendTwoFactorOtp(LoginRequest loginRequest);

    TwoFactorResponseDto verifyOtp(TwoFactorRequestDto dto);

}
