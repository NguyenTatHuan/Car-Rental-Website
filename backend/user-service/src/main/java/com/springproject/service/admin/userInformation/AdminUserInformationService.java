package com.springproject.service.admin.userInformation;

import com.springproject.dto.userinformation.UserInformationDto;
import com.springproject.dto.userinformation.UserInformationUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminUserInformationService {

    List<UserInformationDto> getAllUserInformation();

    UserInformationDto getUserInformationById(UUID id);

    UserInformationDto updateUser(UUID id, UserInformationUpdateDto userInformationUpdateDto);

    void deleteUser(UUID id);

}
