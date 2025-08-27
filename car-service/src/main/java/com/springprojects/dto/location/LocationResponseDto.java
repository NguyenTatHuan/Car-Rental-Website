package com.springprojects.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponseDto {

    private UUID id;

    private String name;

    private String province;

    private String district;

    private String address;

    private String latitude;

    private String longitude;

}
