package com.springproject.service.customer.userinformation;

import com.springproject.dto.userinformation.UserInformationUpdateDto;

import java.util.UUID;

public interface CustomerUserInformationService {

    UserInformationUpdateDto getMyInformation(UUID userId);

    UserInformationUpdateDto updateMyInformation(UUID userId, UserInformationUpdateDto dto);

}
