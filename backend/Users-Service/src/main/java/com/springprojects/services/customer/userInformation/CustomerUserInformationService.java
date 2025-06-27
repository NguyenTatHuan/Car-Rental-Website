package com.springprojects.services.customer.userInformation;

import com.springprojects.dto.UserInformationDto;
import com.springprojects.dto.UserInformationUpdateDto;

import java.util.UUID;

public interface CustomerUserInformationService {

    UserInformationUpdateDto getMyInformation(UUID userId);

    UserInformationUpdateDto updateMyInformation(UUID userId, UserInformationUpdateDto dto);

}
