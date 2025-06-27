package com.springprojects.dto.location;

import lombok.Data;

import java.util.UUID;

@Data
public class LocationResponseDto {

    private UUID id;

    private String name;

    private String province;

    private String district;

    private String address;

    private String latitude;

    private String longitude;

}
