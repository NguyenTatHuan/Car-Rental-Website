package com.springprojects.dto;

import com.springprojects.enums.UserGender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
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
