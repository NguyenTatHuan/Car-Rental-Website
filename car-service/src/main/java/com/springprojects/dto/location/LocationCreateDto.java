package com.springprojects.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocationCreateDto {

    @NotBlank(message = "Location name must not be blank")
    @Size(max = 100, message = "Location name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Province must not be blank")
    @Size(max = 50, message = "Province must be at most 50 characters")
    private String province;

    @NotBlank(message = "District must not be blank")
    @Size(max = 50, message = "District must be at most 50 characters")
    private String district;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Size(max = 30, message = "Latitude must be at most 30 characters")
    private String latitude;

    @Size(max = 30, message = "Longitude must be at most 30 characters")
    private String longitude;

}
