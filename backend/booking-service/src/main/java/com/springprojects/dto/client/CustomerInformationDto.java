package com.springprojects.dto.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInformationDto {

    private String fullName;

    private String email;

}
