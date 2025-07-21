package com.springproject.service.customer.userinformation;

import com.springproject.dto.userinformation.UserInformationUpdateDto;

public interface CustomerUserInformationService {

    UserInformationUpdateDto getMyInformation();

    UserInformationUpdateDto updateMyInformation(UserInformationUpdateDto dto);

}
