package com.springprojects.services.admin.userInformation;

import com.springprojects.dto.userInformation.UserInformationDto;
import com.springprojects.dto.userInformation.UserInformationUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminUserInformationService {

    List<UserInformationDto> getAllUserInformation();

    UserInformationDto getUserInformationById(UUID id);

    UserInformationDto updateUser(UUID id, UserInformationUpdateDto userInformationUpdateDto);

    void deleteUser(UUID id);

}
