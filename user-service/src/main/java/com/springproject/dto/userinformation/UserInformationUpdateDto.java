package com.springproject.dto.userinformation;

import com.springproject.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationUpdateDto {

    private String fullName;

    private String citizenID;

    private LocalDate birthday;

    private UserGender gender;

    private String email;

    private String phone;

    private String address;

    private String nationality;

}
