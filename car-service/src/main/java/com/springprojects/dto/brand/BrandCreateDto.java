package com.springprojects.dto.brand;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BrandCreateDto {

    @NotBlank(message = "Brand name must not be blank")
    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Country must not be blank")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

}
