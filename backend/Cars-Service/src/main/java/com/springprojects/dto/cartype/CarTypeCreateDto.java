package com.springprojects.dto.cartype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarTypeCreateDto {

    @NotBlank(message = "Car Type name must not be blank")
    @Size(max = 100, message = "Car Type name must not exceed 100 characters")
    private String name;

    private String description;

}
